package com.common;

import com.beust.jcommander.JCommander;
import com.dataObjects.Category;
import com.dataObjects.IGCItemList;
import com.dataObjects.Term;
import com.dataObjects.requests.Response;

import java.util.Date;

public class Main {

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
        // Establishing Base URLs to create objects from.
        String testUrl1 = "https://ec2-3-83-75-69.compute-1.amazonaws.com:9443/ibm/iis/igc-rest/v1/";

        //Run tests:
        int test_num = 1;
        try {
//            //// TEST 1 - GET from a public API ////
//              String testUrl2 = "https://dog.ceo/api/breeds/list/";
//            System.out.println("---- BEGINNING TEST1 ----\n");
//            // Establish URLConnection object with base URL.
//            URLConnection connection1 = new URLConnection(testUrl2, true);
//            // Call the GET method for the object, appending the base URL (with specifics).
//            String getTest = connection1.get("all", false);
//            System.out.println("\nTest1 - GET RESPONSE:\n" + getTest);
//            System.out.println("---- ENDING TEST1 ----\n");
//            //// END TEST 1 ////
            test_num++;

            //// TEST 2 - GET from the IGC API ////
            System.out.println("---- BEGINNING TEST2 ----\n");
            // Establish URLConnection object with base URL.
            URLConnection connection = new URLConnection(url, disableSSL,
                    username, password);
            // Call the GET method for the object, appending the base URL (with specifics).
            String getTest2 = connection.get("search/?types=category", true);
            System.out.println("\n---- ENDING TEST2 ----\n");
            //// END TEST 2 ////
            test_num++;

            //// TEST 3 - Perform a IGC GET request and convert ////
            //// the JSON response to a Java object (a Term).  ////
            System.out.println("---- BEGINNING TEST3 ----\n");
            // Call the getIGCTermById method for the object, appending the base URL.
            Term termTest = connection.getIGCTermById(
                    "6662c0f2.e1b1ec6c.00t6i4b9s.oc1te9k.igov6t.hqctlblmgp8tjbb6ad4el"
            );
            // We are performing a GET request for information on a term: "Technical Foul".
            System.out.println("Term object generated:\n\n" + termTest.toString());
            // Try getting Term object variable.
            System.out.println("\nGetting name of Term: " + termTest.getName());
            System.out.println("\n---- ENDING TEST3 ----\n");
            //// END TEST 3 ////
            test_num++;

            //// TEST 4 - Perform a IGC GET request and convert ////
            //// the JSON response to a Java object (a Category).  ////
            System.out.println("---- BEGINNING TEST4 ----\n");
            // Call the getIGCCategoryById method for the object, appending the base URL.
            Category categoryTest = connection.getIGCCategoryById(
                    "6662c0f2.ee6a64fe.00t6i4b6l.2q3r1c0.javi8g.f6vf7ssaf75b7ed3btpqr"
            );
            // We are performing a GET request for information on a category: "Fouls".
            System.out.println("Category object generated:\n\n" + categoryTest.toString());
            // Try getting Category object variable (name).
            System.out.println("\nGetting name of Category: " + categoryTest.getName());
            System.out.println("\n---- ENDING TEST4 ----\n");
            //// END TEST 4 ////
            test_num++;

            //// TEST 5 - Perform a IGC GET request of all categories. ////
            System.out.println("---- BEGINNING TEST5 ----\n");
            // Call the getIGCCategoryList method for the object, entering how many items we want returned.
            IGCItemList categoryList = connection.getIGCCategoryList(100);
            System.out.println("CategoryList object generated\n" /* + categoryList.toString() */);
            // Try getting Category object variable (name).
            System.out.println("\nGetting paging info of categoryList:\n" + categoryList.getPaging());
            System.out.println("\n---- ENDING TEST5 ----\n");
            //// END TEST 5 ////
            test_num++;

            //// TEST 6 - Perform a IGC GET request of all terms. ////
            System.out.println("---- BEGINNING TEST6 ----\n");
            // Call the getIGCTermList method for the object, entering how many items we want returned.
            IGCItemList termList = connection.getIGCTermList(100);
            System.out.println("termList object generated\n" /* + termList.toString() */);
            // Try getting Term object variable (name).
            System.out.println("\nGetting paging info of termList:\n" + termList.getPaging());
            System.out.println("\n---- ENDING TEST6 ----\n");
            //// END TEST 6 ////
            test_num++;

            //// TEST 7 - POST a new category. ////
            System.out.println("---- BEGINNING TEST7 ----\n");
            //Create the new Category object with parent: "Data Directory".
            Category newCategory = new Category(
                    "a_test_cat_1", "Test category created via API",
                    "long description test",
                    "6662c0f2.ee6a64fe.00t6cc6op.9eo1d2k.skhof8.dgog8qrnvlvm4pglk887k"
            );
            // Call the createIGCCategory method with the new Category we created.
            Response postResponse1 = connection.createIGCResource(newCategory);
            // Get the response from the API.
            System.out.println("\nResponse from API:\n" + postResponse1.getMessage() +
                    "\nNew ID: " + postResponse1.get_id() +
                    "\nResponse Code: " + postResponse1.getResponseCode() +
                    "\nResponse Code Message: " + postResponse1.getCodeMessage());
            System.out.println("\n---- ENDING TEST7 ----\n");
            //// END TEST 7 ////
            test_num++;

            //// TEST 8 - POST a new term. ////
            System.out.println("---- BEGINNING TEST8 ----\n");
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
            System.out.println("\nResponse from API:\n" + postResponse2.getMessage() +
                    "\nNew ID: " + postResponse2.get_id() +
                    "\nResponse Code: " + postResponse2.getResponseCode() +
                    "\nResponse Code Message: " + postResponse2.getCodeMessage());
            System.out.println("\n---- ENDING TEST8 ----\n");
            //// END TEST 8 ////
            test_num++;

            //// TEST 9 - PUT a term update. ////
            if (postResponse2.getResponseCode() < 300) {
                System.out.println("---- BEGINNING TEST9 ----\n");
                //Create the new Term object with only the fields we're updating filled out.
                Term updateTerm = new Term(
                        "a_test_term_1_UPDATE", "UPDATED SHORT DESCRIPTION",
                        "UPDATED LONG DESCRIPTION TEST",
                        connection.getTermParentId(postResponse2.get_id()),
                        "ACCEPTED"
                        //Using same parent_category_id as original term. Test10 pulls existing parent id from API.
                );
                String idToUpdate = postResponse2.get_id(); //Get ID of term we created.
                // Call the updateIGCResource method with the new Term we created.
                System.out.println("Attempting to update Term in API\n");
                Response putResponse = connection.updateIGCResource(idToUpdate, updateTerm);
                // Get the response from the API.
                System.out.println("\nResponse from API:\n" + putResponse.getMessage() +
                        "\nNew ID: " + putResponse.get_id() +
                        "\nResponse Code: " + putResponse.getResponseCode() +
                        "\nResponse Code Message: " + putResponse.getCodeMessage());
                System.out.println("\n---- ENDING TEST9 ----\n");
                //// END TEST 9 ////
            } else {
                System.out.println("\n-- Skipping Test9. No new term to update.\n");
            }
            test_num++;


            //// TEST 10 - PUT a category update. ////
            if (postResponse1.getResponseCode() < 300) {
                System.out.println("---- BEGINNING TEST10 ----\n");
                //Create the new Category object.
                Category updateCategory = new Category(
                        "a_test_category_1_UPDATE", "UPDATED SHORT DESCRIPTION",
                        "UPDATED LONG DESCRIPTION TEST",
                        connection.getCategoryParentId(postResponse1.get_id())
                        //Above line is getting the ID of the parent category of the category we created in Test7.
                );
                String idToUpdate2 = postResponse1.get_id(); //Get ID of category we created
                // Call the updateIGCResource method with the new Category we created.
                System.out.println("Attempting to update Category in API\n");
                Response putResponse2 = connection.updateIGCResource(idToUpdate2, updateCategory);
                // Get the response from the API.
                System.out.println("\nResponse from API:\n" + putResponse2.getMessage() +
                        "\nNew ID: " + putResponse2.get_id() +
                        "\nResponse Code: " + putResponse2.getResponseCode() +
                                "\nResponse Code Message: " + putResponse2.getCodeMessage());
                System.out.println("\n---- ENDING TEST10 ----\n");
                //// END TEST 10 ////
            } else {
                System.out.println("\n-- Skipping Test10. No new category to update.\n");
            }
            test_num++;

            //// TEST 11 - DELETE a category. ////
            if (postResponse1.getResponseCode() < 300) {
                System.out.println("---- BEGINNING TEST11 ----\n");
                String idToDelete1 = postResponse1.get_id(); //Get ID of category we created
                // Call the deleteIGCResource method with the ID of the Category we created.
                System.out.println("Attempting to delete Category in API\n");
                Response deleteResponse1 = connection.deleteIGCResource(idToDelete1);
                // Read the response from the API.
                System.out.println("\nResponse from API:\n" + deleteResponse1.getMessage() +
                        "\nNew ID: " + deleteResponse1.get_id() +
                        "\nResponse Code: " + deleteResponse1.getResponseCode() +
                        "\nResponse Code Message: " + deleteResponse1.getCodeMessage());
                System.out.println("\n---- ENDING TEST11 ----\n");
                //// END TEST 11 ////
            } else {
                System.out.println("\n-- Skipping Test11. No new category to delete.\n");
            }
            test_num++;

            //// TEST 12 - DELETE a term. ////
            if (postResponse2.getResponseCode() < 300) {
                System.out.println("---- BEGINNING TEST12 ----\n");
                String idToDelete2 = postResponse2.get_id(); //Get ID of term we created
                // Call the deleteIGCResource method with the ID of the term we created.
                System.out.println("Attempting to delete Term in API\n");
                Response deleteResponse2 = connection.deleteIGCResource(idToDelete2);
                // Read the response from the API.
                System.out.println("\nResponse from API:\n" + deleteResponse2.getMessage() +
                        "\nNew ID: " + deleteResponse2.get_id() +
                        "\nResponse Code: " + deleteResponse2.getResponseCode() +
                        "\nResponse Code Message: " + deleteResponse2.getCodeMessage());
                System.out.println("\n---- ENDING TEST12 ----\n");
                //// END TEST 12 ////
            } else {
                System.out.println("\n-- Skipping Test12. No new term to delete.\n");
            }
            test_num++;

            //// TEST 13 - POST Search for a resource by name ////
            System.out.println("---- BEGINNING TEST13 ----\n");
            // Call the search method and get a response
            System.out.println("Submitting search request for resources with 'test' in name");
            IGCItemList itemList13 = connection.searchIGCResourceName("test");
            //Print results
            System.out.println("FULL SEARCH RESULTS:\n" + itemList13);
            System.out.println("\n---- ENDING TEST13 ----\n");
            //// END TEST 13 ////
            test_num++;

            //// TEST 14 - POST Search for a resource modified between two date-times ////
            System.out.println("---- BEGINNING TEST14 ----\n");
            // Get current time.
            long curTime = new Date().getTime();
            // Call the search method and get a response
            System.out.println("Submitting search request for resources modified in last week");
            IGCItemList itemList14 = connection.searchIGCResourceModifiedBetween(
                    "2019/03/15 00:00:00",
                    "2019/03/20 00:00:00"
            );
            //Print results
            System.out.println("SEARCH RESULTS paging info:\n" + itemList14.getPaging());
            System.out.println("\n---- ENDING TEST14 ----\n");
            //// END TEST 14 ////
            test_num++;

            //// TEST 15 - POST Search for a resource by search term ////
            System.out.println("---- BEGINNING TEST15 ----\n");
            // Call the search method and get a response
            System.out.println("Submitting search request");
            IGCItemList itemList15 = connection.searchIGCResource("test");
            //Print results
            System.out.println("SEARCH RESULTS paging info:\n" + itemList15.getPaging());
            System.out.println("\n---- ENDING TEST15 ----\n");
            //// END TEST 15 ////
            test_num++;

            //// TEST 16 - POST Search for a resource by user ////
            System.out.println("---- BEGINNING TEST16 ----\n");
            // Call the search method and get a response
            System.out.println("Submitting search request");
            IGCItemList itemList16 = connection.searchIGCResourceByUser("API");
            //Print results
            System.out.println("SEARCH RESULTS paging info:\n" + itemList16.getPaging());
            System.out.println("\n---- ENDING TEST16 ----\n");
            //// END TEST 16 ////
            test_num++;

//            //// TEST 17 - updateIGCTermName method ////
//            System.out.println("---- Beginning TEST17 ----\n");
//            //See if term to update exists
//            if (postResponse2.getResponseCode() < 300) {
//                Response response17 = connection.updateIGCTermName(postResponse2.get_id(), "Name_Update_Test");
//                System.out.println("TERM NAME UPDATE RESPONSE:\n" + response17);
//            } else {
//                System.out.println("No term to update... Aborting test");
//            }
//            System.out.println("\n---- ENDING TEST17 ----\n");
//            //// END TEST 17 ////
//            test_num++;

            System.out.println("Testing complete: Completed " + (test_num-1) + " tests.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n[Error on Test"+ (test_num) +"]\n");
        }
    }
}
