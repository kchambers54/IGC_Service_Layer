package com.common;

import com.beust.jcommander.JCommander;
import com.dataObjects.Category;
import com.dataObjects.IGCItemList;
import com.dataObjects.IGCResource;
import com.dataObjects.Term;
import com.dataObjects.requests.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import java.util.Date;

public class Main {

    private static Logger logger;

    /**
     * Main method for running test script from command line.
     * @param argv input arguments: ('String url', 'boolean disableSSL',
     *             'String username', 'String password')
     */
    public static void main(String[] argv) {
        Args args = new Args();
        JCommander jct = JCommander.newBuilder()
                .addObject(args)
                .build();
        jct.parse(argv);
        if (args.isHelp()) {
            jct.usage();
        } else {
            if (args.isDebug()) { // Enables verbose debugging mode.
                System.out.println("DEBUG MODE");
                System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
            }
            logger = LoggerFactory.getLogger(Main.class);

            tests(args.getUrl(), args.isDisableSslVerification(), args.getUsername(), args.getPassword());
        }
    }

    /**
     * Essentially a test script that runs through several URLConnection methods.
     * @param url Base URL of API. For IGC, include everything up to '.../v1/'.
     * @param disableSSL Setting to 'true' will disable SSL Cert verification.
     * @param username Username to be used for HTTP requests.
     * @param password Password to be used for HTTP requests.
     */
    private static void tests(String url, boolean disableSSL, String username, String password) {
        // Base URL for IGC API at time of code creation (2019/03/19).
        String apiUrl = "https://ec2-3-83-75-69.compute-1.amazonaws.com:9443/ibm/iis/igc-rest/v1/";

        //Run tests:
        int test_num = 1;
        try {
//            //// TEST 1 - GET from a public API //// - Can be run to ensure connection to a public API
//              String testUrl2 = "https://dog.ceo/api/breeds/list/";
//            logger.info("---- BEGINNING TEST1 ----\n");
//            // Establish URLConnection object with base URL.
//            URLConnection connection1 = new URLConnection(testUrl2, true);
//            // Call the GET method for the object, appending the base URL (with specifics).
//            String getTest = connection1.get("all", false);
//            logger.info("\nTest1 - GET RESPONSE:\n" + getTest);
//            logger.info("---- ENDING TEST1 ----\n");
//            //// END TEST 1 ////
            test_num++;

//            //// TEST 2 - GET from the IGC API ////
//            logger.info("---- BEGINNING TEST2 ----\n");
//            // Establish URLConnection object with base URL.
            URLConnection connection = new URLConnection(url, disableSSL,
                    username, password);
//            // Call the GET method for the object, appending the base URL (with specifics).
//            String getTest2 = connection.get("search/?types=category", true);
//            logger.info("---- ENDING TEST2 ----\n");
//            //// END TEST 2 ////
            test_num++;

            //// TEST 3 - Perform a IGC GET request and convert ////
            //// the JSON response to a Java object (a Term).  ////
            logger.info("---- BEGINNING TEST3: getIGCResourceById to get Term\n");
            // Call the getIGCResourceById method on a Term's ID.
            IGCResource termTest = connection.getIGCResourceById(
                    "6662c0f2.e1b1ec6c.00t6i4b9s.oc1te9k.igov6t.hqctlblmgp8tjbb6ad4el"
            );
            // We are performing a GET request for information on a term: "Technical Foul".
            logger.debug("Term object generated:\n" + termTest.toString());
            // Try getting Term object variable.
            logger.info("\nGetting name of Term: " + termTest.getName() + "\n");
            logger.info("---- ENDING TEST3 ----\n");
            //// END TEST 3 ////
            test_num++;

            //// TEST 4 - Perform a IGC GET request and convert ////
            //// the JSON response to a Java object (a Category).  ////
            logger.info("---- BEGINNING TEST4: getIGCResourceById to get category\n");
            // Call the getIGCResourceById method on a category's ID.
            IGCResource categoryTest = connection.getIGCResourceById(
                    "6662c0f2.ee6a64fe.00t6i4b6l.2q3r1c0.javi8g.f6vf7ssaf75b7ed3btpqr"
            );
            // We are performing a GET request for information on a category: "Fouls".
            logger.debug("Category object generated:\n" + categoryTest.toString());
            // Try getting Category object variable (name).
            logger.info("\nGetting name of Category: " + categoryTest.getName() + "\n");
            logger.info("---- ENDING TEST4 ----\n");
            //// END TEST 4 ////
            test_num++;

            //// TEST 5 - Perform a IGC GET request of all categories. ////
            logger.info("---- BEGINNING TEST5: getIGCCategoryList()\n");
            // Call the getIGCCategoryList method for the object, entering how many items we want returned.
            IGCItemList categoryList = connection.getIGCCategoryList(100);
            logger.debug("CategoryList received\n" + categoryList.toString());
            // Try getting Category object variable (name).
            logger.info("\nNumber of results: " + categoryList.getPaging().getNumTotal() + "\n");
            logger.info("---- ENDING TEST5 ----\n");
            //// END TEST 5 ////
            test_num++;

            //// TEST 6 - Perform a IGC GET request of all terms. ////
            logger.info("---- BEGINNING TEST6: getIGCItemList()\n");
            // Call the getIGCTermList method for the object, entering how many items we want returned.
            IGCItemList termList = connection.getIGCTermList(100);
            logger.debug("termList object generated\n" + termList.toString());
            // Try getting Term object variable (name).
            logger.info("\nNumber of results: " + termList.getPaging().getNumTotal() + "\n");
            logger.info("---- ENDING TEST6 ----\n");
            //// END TEST 6 ////
            test_num++;

            //// TEST 7 - POST a new category. ////
            logger.info("---- BEGINNING TEST7: Create new Category (Data Dictionary as parent)\n");
            //Create the new Category object with parent: "Data Directory".
            Category newCategory = new Category(
                    "a_test_cat_1", "Test category created via API",
                    "long description test",
                    "6662c0f2.ee6a64fe.00t6cc6op.9eo1d2k.skhof8.dgog8qrnvlvm4pglk887k"
            );
            // Call the createIGCCategory method with the new Category we created.
            Response postResponse1 = connection.createIGCResource(newCategory);
            // Get the response from the API.
            logger.info("Response Code: " + postResponse1.getResponseCode() +
                    "\nResponse Code Message: " + postResponse1.getCodeMessage());
            logger.debug("Message:\n" + postResponse1.getMessage() +
                    "\nNew ID: " + postResponse1.get_id() + "\n");
            logger.info("---- ENDING TEST7 ----\n");
            //// END TEST 7 ////
            test_num++;

            //// TEST 8 - POST a new term. ////
            logger.info("---- BEGINNING TEST8: Create new Term (DataDictionary as parent)\n");
            //Create the new Term object with parent: "Data Directory".
            Term newTerm = new Term(
                    "a_test_term_1", "Test term created via API",
                    "long description test",
                    "6662c0f2.ee6a64fe.00t6cc6op.9eo1d2k.skhof8.dgog8qrnvlvm4pglk887k",
                    "CANDIDATE"
            );
            // Call the createIGCTerm method with the new Term we created.
            Response postResponse2 = connection.createIGCResource(newTerm);
            // Get the response from the API.
            logger.info("Response Code: " + postResponse2.getResponseCode() +
                    "\nResponse Code Message: " + postResponse2.getCodeMessage());
            logger.debug("\nResponse from API:\n" + postResponse2.getMessage() +
                    "\nNew ID: " + postResponse2.get_id() + "\n");
            logger.info("---- ENDING TEST8 ----\n");
            //// END TEST 8 ////
            test_num++;

            //// TEST 9 - PUT a term update. ////
            if (postResponse2.getResponseCode() < 300) {
                logger.info("---- BEGINNING TEST9: Update the Term we created in test8\n");
                //Create the new Term object with only the fields we're updating filled out.
                Term updateTerm = new Term(
                        "a_test_term_1_UPDATE", "UPDATED SHORT DESCRIPTION",
                        "UPDATED LONG DESCRIPTION TEST",
                        connection.getResourceParentId(postResponse2.get_id()),
                        "ACCEPTED"
                        //Using same parent_category_id as original term. Test10 pulls existing parent id from API.
                );
                String idToUpdate = postResponse2.get_id(); //Get ID of term we created.
                // Call the updateIGCResource method with the new Term we created.
                Response putResponse = connection.updateIGCResource(idToUpdate, updateTerm);
                // Get the response from the API.
                logger.info("\nResponse Code: " + putResponse.getResponseCode() +
                        "\nResponse Code Message: " + putResponse.getCodeMessage());
                logger.debug("\nResponse from API:\n" + putResponse.getMessage() +
                        "\nNew ID: " + putResponse.get_id() + "\n");
                logger.info("---- ENDING TEST9 ----\n");
                //// END TEST 9 ////
            } else {
                logger.warn("\n-- Skipping Test9. No new term to update.\n");
            }
            test_num++;


            //// TEST 10 - PUT a category update. ////
            if (postResponse1.getResponseCode() < 300) {
                logger.info("---- BEGINNING TEST10: Update the Category we created in test7\n");
                //Create the new Category object.
                Category updateCategory = new Category(
                        "a_test_category_1_UPDATE", "UPDATED SHORT DESCRIPTION",
                        "UPDATED LONG DESCRIPTION TEST",
                        connection.getResourceParentId(postResponse1.get_id())
                        //Above line is getting the ID of the parent category of the category we created in Test7.
                );
                String idToUpdate2 = postResponse1.get_id(); //Get ID of category we created
                // Call the updateIGCResource method with the new Category we created.
                Response putResponse2 = connection.updateIGCResource(idToUpdate2, updateCategory);
                // Get the response from the API.
                logger.info("Response Code: " + putResponse2.getResponseCode() +
                        "\nResponse Code Message: " + putResponse2.getCodeMessage());
                logger.debug("\nResponse from API:\n" + putResponse2.getMessage() +
                        "\nNew ID: " + putResponse2.get_id() + "\n");
                logger.info("---- ENDING TEST10 ----\n");
                //// END TEST 10 ////
            } else {
                logger.warn("\n-- Skipping Test10. No new category to update.\n");
            }
            test_num++;

            //// TEST 11 - DELETE a category. ////
            if (postResponse1.getResponseCode() < 300) {
                logger.info("---- BEGINNING TEST11: Delete the Category we created in test7\n");
                String idToDelete1 = postResponse1.get_id(); //Get ID of category we created
                // Call the deleteIGCResource method with the ID of the Category we created.
                Response deleteResponse1 = connection.deleteIGCResource(idToDelete1);
                // Read the response from the API.
                logger.info("Response Code: " + deleteResponse1.getResponseCode() +
                        "\nResponse Code Message: " + deleteResponse1.getCodeMessage());
                logger.debug("\nResponse from API:\n" + deleteResponse1.getMessage() +
                        "\nNew ID: " + deleteResponse1.get_id() + "\n");
                logger.info("---- ENDING TEST11 ----\n");
                //// END TEST 11 ////
            } else {
                logger.warn("\n-- Skipping Test11. No new category to delete.\n");
            }
            test_num++;

            //// TEST 12 - DELETE a term. ////
            if (postResponse2.getResponseCode() < 300) {
                logger.info("---- BEGINNING TEST12: Delete the Term we created in test8\n");
                String idToDelete2 = postResponse2.get_id(); //Get ID of term we created
                // Call the deleteIGCResource method with the ID of the term we created.
                Response deleteResponse2 = connection.deleteIGCResource(idToDelete2);
                // Read the response from the API.
                logger.info("\nResponse Code: " + deleteResponse2.getResponseCode() +
                        "\nResponse Code Message: " + deleteResponse2.getCodeMessage());
                logger.debug("\nResponse from API:\n" + deleteResponse2.getMessage() +
                        "\nNew ID: " + deleteResponse2.get_id() + "\n");
                logger.info("---- ENDING TEST12 ----\n");
                //// END TEST 12 ////
            } else {
                logger.warn("\n-- Skipping Test12. No new term to delete.\n");
            }
            test_num++;

            //// TEST 13 - POST Search for a resource by name ////
            logger.info("---- BEGINNING TEST13: Search for Resources with name=test\n");
            // Call the search method and get a response
            logger.debug("Submitting search request for resources with 'test' in name");
            IGCItemList itemList13 = connection.searchIGCResourceName("test");
            //Print results
            logger.info("Number of search results: " + itemList13.getPaging().getNumTotal() + "\n");
            logger.info("---- ENDING TEST13 ----\n");
            //// END TEST 13 ////
            test_num++;

            //// TEST 14 - POST Search for a resource modified between two date-times ////
            logger.info("---- BEGINNING TEST14: Search for Resources modified between 2019/01/01 and 2019/02/01\n");
            // Get current time.
            long curTime = new Date().getTime();
            // Call the search method and get a response
            logger.debug("Submitting search request for resources modified between two times");
            IGCItemList itemList14 = connection.searchIGCResourceModifiedBetween(
                    "2019/01/01 00:00:00",
                    "2019/02/01 00:00:00"
            );
            //Print results
            logger.info("Number of search results: " + itemList14.getPaging().getNumTotal() + "\n");
            logger.info("---- ENDING TEST14 ----\n");
            //// END TEST 14 ////
            test_num++;

            //// TEST 15 - POST Search for a resource by search term ////
            logger.info("---- BEGINNING TEST15: Search for Resources by search term 'test'\n");
            // Call the search method and get a response
            logger.debug("Submitting search request for search term: test");
            IGCItemList itemList15 = connection.searchIGCResource("test");
            //Print results
            logger.info("Number of search results: " + itemList15.getPaging().getNumTotal() + "\n");
            logger.info("---- ENDING TEST15 ----\n");
            //// END TEST 15 ////
            test_num++;

            //// TEST 16 - POST Search for a resource by user ////
            logger.info("---- BEGINNING TEST16: Search for Resources by user: 'API'\n");
            // Call the search method and get a response
            logger.debug("Submitting search request by user");
            IGCItemList itemList16 = connection.searchIGCResourceByUser("API");
            //Print results
            logger.info("Number of search results: " + itemList16.getPaging().getNumTotal() + "\n");
            logger.info("---- ENDING TEST16 ----\n");
            //// END TEST 16 ////
            test_num++;

            //// TEST 17 - Test isResourceOfType() on 'Data Dictionary'////
            logger.info("---- BEGINNING TEST17: Test isResourceOfType to confirm that 'Data Dictionary' is a Category");
            boolean result17 = connection.isResourceOfType(
                    "6662c0f2.ee6a64fe.00t6cc6op.9eo1d2k.skhof8.dgog8qrnvlvm4pglk887k", //ID of 'Data Dictionary'
                    "category");
            logger.info("ID of a category is found to be a category: " + result17);
            logger.info("---- ENDING TEST16 ----\n");
            //// END TEST 17 ////
            test_num++;

//            //// TEST 18 - updateIGCTermName method ////
//            logger.info("---- Beginning TEST18 ----\n");
//            //See if term to update exists
//            if (postResponse2.getResponseCode() < 300) {
//                Response response18 = connection.updateIGCTermName(postResponse2.get_id(), "Name_Update_Test");
//                logger.debug("TERM NAME UPDATE RESPONSE:\n" + response18);
//            } else {
//                logger.warn("No term to update... Aborting test");
//            }
//            logger.info("\n---- ENDING TEST18 ----\n");
//            //// END TEST 18 ////
//            test_num++;

            System.out.println("Testing complete: Completed " + (test_num-1) + " tests.");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("\n[Error on Test"+ (test_num) +"]\n");
        }
    }
}
