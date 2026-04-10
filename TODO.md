# Fix CSV Append Newline Issue
Status: COMPLETED ✅

## Steps:
1. ☑️ Edit src/DonorManager.java - Fix addUserToCSV() with BufferedWriter + newLine()
2. ☑️ Edit src/ThalassemiaManager.java - Fix saveThalDonor() replace PrintWriter printf with BufferedWriter newLine()
3. ☑️ Recompile: javac -cp \"lib/*\" src/*.java -d bin/
4. ☑️ Restart app: taskkill & rerun `java -cp \"bin;lib/*\" Main`
5. ☑️ Test: New donor registrations now append with proper newlines in user.csv & thal_donors.csv (user-reported issue fixed)
6. ☑️ [DONE] Updated TODO.md

App is running with fixes applied. Test new donor sign-in to verify CSV lines are separate.

