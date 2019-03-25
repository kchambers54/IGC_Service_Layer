IGC_Service_Layer - Keller Chambers, 2019
__________________

REQUIRED LIBS:
- gson-2.6.2.jar           // Converting JSON to POJO and vice versa
- jcommander-1.72.jar      // To run tests from command line
- slf4j-api-1.7.26.jar     // Logging facade
- slf4j-simple-1.7.26.jar  // Used as simple plugin for logging facade. Could be replaced with various other loggers
__________________

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
___________________________

UPKEEP NOTES (3/21/2019):

	1. NEW POJO FIELDS: New custom fields will need to be added to Term and Category as they are introduced to
	IGC Resources.
		a. When adding a new field that has spaces in IGC, be sure to include a "@SerializedName()" annotation
		with the IGC version of the fields name --> See Category.custom_Contains_PII
	2. NEW IGC RESOURCE TYPES: New POJOs will need to be created (currently only have Categories and Terms) as
	new Resource types are introduced to IGC.
		a. As this is done, may need to remove some of the content of the common IGCResource parent abstract
		class (as it becomes no longer common across all resources).
	3. LOGGING: Logging currently uses SLF4J -> SLF4J simple.
	4. COMMAND LINE ARGUMENTS: Command Line input arguments (JCommander) can be modified within the Args() class.
_____________________________

KNOWN ISSUE (3/21/2019):

	1. SERVER ISSUES?: Have been getting the below errors on occasion.  Generally everything works well, but
	occasionally the errors below will pup up and continue until either the server is reset, or some unknown
	amount of time has passed.
	    - 400 Bad Request:  when doing a POST-search that would return more than 5 results.
	    - 500 Server Error: when trying to POST-create the term seen in 'test8' of the tests() script.
______________________________

IMPORTANT ADDITIONAL NOTES ON FAILED REQUEST RESULTS:

    1. FAILED POST SEARCH RESPONSES: When an http request returns a code of 300 or greater, an empty IGCItemList
    is generated. Empty in that all values String are set to "", and 'failedRequest' is set to true.
    This is done to avoid NullPointerExceptions when returned objects are later called upon.
        - There is a commented-out if-else block in URLConnection.searchIGC() that can be un-commented to
          replace this empty-object return strategy with throwing an IllegalStateException.

    2. FAILED GET-BASED METHOD: Like POST-searches mentioned in #1 above, creates an empty placeholder IGCResource
    to avoid later code running into NullPointerExceptions.
        - This empty-object return strategy can be converted to Exception throwing like above if desired.
        - Empty placeholder IGCResources can be distinguished by their 'failedResponse' field being set to true.

    *** Any POJO Resource that is from a failed request will have 'failedRequest' set to true ***

_______________________________