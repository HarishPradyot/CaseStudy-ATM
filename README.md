# CaseStudy-ATM

* The Username and Password for the Admin is provided in the first line of the "AccessLog.txt" file and can be modified by the Admin implicitly if required.
* Please change the path of the files used in the code to their complete path if the Program is unable to access them.
* "Cash.txt" is used to keep track of the number of notes of different denominations available at the ATM when it is shut down by the Admin. Hence, it can be reloaded when the ATM is running again.
* "AccessLog.txt" is used to record all the remote login attempts made by the Admin.
* Client Data is stored in the "DataBase.csv" file.
* Any user who has not yet registered in the Bank's ATM has an option to create an account by depositing an initial amount. And his credentials are displayed on the "NewClient.txt" file. It is cleared on confirmation from the Client so that it will be inaccessible to the next person accessing the terminal.
* All Credentials, be it Users or Clients, are collected using separate Panels (using GUI), instead of using terminal input, so that it will be inaccessible to the next person accessing the terminal.
* Bank Statement upon request by the Client is generated in the "Statement.csv" file. It is cleared on confirmation from the Client so that it will be inaccessible to the next person accessing the terminal.
* Record of the transactions for access to the Admin is stored in the "Transaction.csv" file.

* User/Client functionality: ViewAccount, Deposit, Withdraw, Change PIN, Request BankStatement, International Money Transfer (to JPY, EUR, USD)
* Admin functionality: Add cash to the cash dispenser, modify the maximum withdrawal limit. Also implicitly access all the files "DataBase.csv"; "Transactions.csv"; "AccessLog.txt"; "Cash.txt" 

### Limitations
* International Money Transfers are superficial, and the International Accounts of the clients are not stored.
* The local currency used by the ATM is INR though it is not explicitly mentioned anywhere to the Client
* Authentication by providing valid credentials is not backed up by blocking ATM card after a finite number of Invalid Attempts.
* Messages printed in the terminal have not been appropriately formatted using '\n' etc.
* IFSC code and processing fee is not taken care of.
