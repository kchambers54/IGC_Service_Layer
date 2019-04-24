IGC_Service_Layer - Keller Chambers, 2019
__________________

Current Version:
v1.1 - AWS Lambda Conversion
 - Added LambdaHandler class to replace Main class.
    - Handles incoming requests when running on AWS Lambda.
 - Deleted contents of Main.main() where tests were run in v1.0.
    - No longer requires JCommander lib.

Past Versions:
- v1.0 - Initial Release
__________________

REQUIRED LIBS:
NEW:
- aws-lambda-java-core-1.2.0.jar  // Compatability with AWS Lambda
EXISTING:
- gson-2.6.2.jar           // Converting JSON to POJO and vice versa
- slf4j-api-1.7.26.jar     // Logging facade
- slf4j-simple-1.7.26.jar  // Used as simple plugin for logging facade. Could be replaced with various other loggers

NO LONGER REQUIRED:
- jcommander-1.72.jar      // To run tests from command line
__________________

RUNNING IN AWS LAMBDA (4/24/2019):

    COMPILING AND UPLOADING:
    1. Compile into an executable jar.
    2. Upload to AWS Lambda function "IGC-Java-Service-Layer".
    3. Set Lambda Environment Variables for:
        a. 'URL': IGC API URL.
        b. 'disableSSL': disables SSL cert verification. Required as of 4/24/19.
    4. Either run tests or set up other input into Lambda (such as AWS API Gateway).

    SENDING JSON TO LAMBDA:
    Incoming JSON to Lambda should follow the formats below:

    1. Search Request
    {
      "username": "USERNAME",
      "password": "PASSWORD",
      "function": "searchIGCResource",      // There are various search functions
      "pageSize": 100,                      // How many results to return.
      "search": {
        "term": "SEARCH_TERM",              // Term to search for
        "prop": "PROPERTY_TO_CHECK",        // Resource property to check for term (Used in custom searches)
        "startDate": "yyyy/MM/dd hh:mm:ss", // Start date for time-based searches
        "endDate": "yyyy/MM/dd hh:mm:ss"    // End date for time-based searches
      },
      "resource": {},
      "update": {}
    }

    2. Get Request
    {
      "username": "igc.api.user",
      "password": "igc.api.user",
      "function": "getIGCResourceById",     // There are various get request functions
      "resource": {
        "_id": "6662c0f2.ee6a64fe.00t6kiltn.ifpp3fs.bkiqah.59kntr89opbn0slqkudt1" // id of Resource to get.
      }
    }

    3. Create Request (All fields in 'request: {_}' must be filled)
    {
      "username": "igc.api.user",
      "password": "igc.api.user",
      "function": "createIGCResource",
      "resource": {
        "_type": "term",            // Resource type to create. Must have a POJO (term, category).
        "name": "test_term",
        "short_description": "term for testing",
        "long_description": "long description here...",
        "parent_category_id": "6662c0f2.ee6a64fe.00t6kiqpq.d8bbm9s.v9nomg.9ddtdnc4ktdg7oh643doa",
        "status": "ACCEPTED"        // Can be 'ACCEPTED', 'CANDIDATE', 'STANDARD', etc...
      }
    }

    4. Update Request
    {
      "username": "igc.api.user",
      "password": "igc.api.user",
      "function": "updateIGCResource",
      "resource": {                     // Existing resource targeted for update. '_id' and '_type' required.
        "_id": "6662c0f2.ee6a64fe.00t6kiltn.ifpp3fs.bkiqah.59kntr89opbn0slqkudt1",
        "_type": "category"
      },
      "update": {                 // Properties to update in resource (include only what is being updated)
        "long_description": "Update - 1:51"
      }
    }

    5. Delete Request
    {
      "username": "igc.api.user",
      "password": "igc.api.user",
      "function": "deleteIGCResource",
      "resource": {
        "_id": "6662c0f2.ee6a64fe.00t6kiltn.ifpp3fs.bkiqah.59kntr89opbn0slqkudt1"  // Resource to delete
      }
    }
___________________________

UPKEEP NOTES (4/24/2019):

	1. NEW POJO FIELDS: New custom fields will need to be added to Term and Category as they are introduced in
	IGC.
		a. When adding a new field that has spaces in IGC, be sure to include a "@SerializedName()" annotation
		with the IGC version of the field's name --> See Category.custom_Contains_PII for example.
	2. NEW IGC RESOURCE TYPES: New POJOs will need to be created (currently only have Categories and Terms) as
	new Resource types are introduced to IGC.
		a. As this is done, may need to remove some of the content of the IGCResource parent abstract
		class (as fields become no longer common across all resources).
		b. Will also need to update 'LambdaHandler.callUpdateIGCResource()',
		'LambdaHandler.callCreateIGCResource()', 'URLConnection.getIGCResource()', and 'JsonToObject.to____()'.
		    --> All methods mentioned in 'b.' above are marked with "TODO's".
	3. LOGGING: Logging currently uses SLF4J -> SLF4J simple.
_____________________________

KNOWN ISSUE (4/24/2019):

	1. IGC SERVER ISSUES?: Have been getting the below errors on occasion.  Generally everything works well, but
	occasionally the errors below will pop up and continue until either the server is reset, or some unknown
	amount of time has passed.
	    - 400 Bad Request:  when doing a POST-search that would return more than 5 results.
	    - 500 Server Error: when trying to POST-create a term or category.
______________________________

IMPORTANT ADDITIONAL NOTES ON FAILED REQUEST RESULTS:

    1. FAILED POST SEARCH RESPONSES: When an http request returns a code of 300 or greater, an empty IGCItemList
    is generated. Empty in that all values String are set to "", and 'failedRequest' is set to true.
    This is done to avoid NullPointerExceptions when returned objects are later called upon.
        - There is a commented-out if-else block in URLConnection.searchIGC() that can be un-commented to
          replace this empty-object return strategy with throwing an IllegalStateException.

    2. FAILED GET-BASED METHOD: Like POST-searches mentioned in #1 above, creates an empty placeholder
    IGCResource to avoid later code running into NullPointerExceptions.
        - This empty-object return strategy can be converted to Exception throwing like above if desired.
        - Empty placeholder IGCResources can be distinguished by their 'failedResponse' field being set to true.



______________________________
______________________________
- - - END CURRENT README - - -
______________________________
______________________________




Everything below is from v1.0 - No longer applicable...
______________________________

RUNNING TESTING SCRIPT FROM THE COMMAND LINE:

>> java -jar IGC_Service_Layer.jar

ARGUMENT TAGS:
    -url
      URL to base all requests from. Default is IGC API
      Default: https://ec2-3-83-75-69.compute-1.amazonaws.com:9443/ibm/iis/igc-rest/v1/  //OUR IGC API
    -ssl
      Disable SSL Cert Verification      // As of 3_21_2019, required to access API.
      Default: false
    -user, -username
      Username for all HTTP requests
    -pw, -password
      Password for all HTTP requests
    --debug, -d
      enables verbose debugging
      Default: false
    --help, -h
      Prints this info page

_______________________

COMMAND LINE ARGUMENTS: Command Line input arguments (JCommander) can be modified within the Args() class.
