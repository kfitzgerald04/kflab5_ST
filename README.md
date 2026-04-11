# LAB 5 API TESTING

NOTES ON HOW TO FIND EVERYTHING (logistics)
- studentRegDemo contains all of my source files!
- The design document can be found in my resources folder in MAIN
- The POSTMAN collection test suite can be found in my resources folder in TEST (you should be able to copy my collection into postman and select tests to run)

RUNNING INSTRUCTIONS (if needed)
- cd STUDENTREGDEMO
- mvn springboot:run
- This should allow the collections to be ran in POSTMAN

FOR UI RUN
- mvn spring-boot:run in one terminal
- cd fronted --> npm run dev in another terminal
- mvn verify in another terminal to run UI tests

ADDTIONAL COMMENTS ON THE STRUCTURE OF MY TESTS
- As there were only 2 main EPs, I only have 1 TEST CASE for it, that sort of groups those together. I didn't test error values in EP testing, they are caught in BVA!
- For the BVA test cases, you will notice that my tests are grouped by outcome. I do not have individual tests for each BV, however I make sure that my POSTMAN tests capture
at least the boundaries immediately above and below the thresholds. I didn't want to be too specific there, as it isn't necessarily the point of this lab :)
- All values have respective TCIs, they may not however appear in the Test Case table. ([] indicates same outcome/expected results)'
- The CRUD tests, follows a state transition guideline, it included both valid/invalid transitions. Think of it as more like cause & effect!

Thanks!
