Coco Bank

Introduction
The program is designed to provide users with the ability to perform a range of
financial operations, including tracking expenses, making deposits, and
managing their accounts. It has been developed using the Kotlin(Android Compose), employing an object-oriented approach to create a modular and
interactive program structure. We used the following technologies for the backend: Spring boot, Spring security, SQl, and a server on Microsoft Azure. The user interface of the program has been developed in English, offering a user-friendly and
intuitive experience. Whether you are an individual looking to manage your
personal finances or a small business in need of a reliable financial management
solution, this program is designed to meet your needs. In the following
documentation, you will find detailed instructions on installation, configuration,
and utilization of the program, along with code examples and troubleshooting
information.


........................................................

Requirements
Minimum system requirements for the program:
Operating System: Android version 5.0 or higher	
Memory: Minimum of 2 GB RAM
Hard Disk Space: Minimum of 50 MB for program installation
Other Requirements: High-speed internet connection for certain features or
online functionality, availability of the latest drivers and updates for the
operating system and hardware components
Please note that these are minimum requirements, and actual requirements may
vary depending on the size of the program, its features, and other factors. It is
also recommended to have up-to-date operating system updates and drivers for
optimal program performance.
Installation
To install and run the program, please follow these steps:
1 Download the program folder from the provided source.
2 Locate the downloaded folder on your computer.
3 Open the folder and look for the executable (.exe) file.
4 Double-click the executable file to launch the program.
By executing the .exe file, the program will be launched, and you will be able to
access its features and functionalities. It's important to ensure that you have
downloaded the complete program folder and have the necessary permissions to
run executable files on your computer.
Note: In some cases, you might need to extract the downloaded folder from a
compressed archive (e.g., a ZIP file) before being able to access the .exe file. In
such cases, use a file extraction tool (such as WinRAR, 7-Zip, or the built-in
extraction utility in your operating system) to extract the folder before running
the executable file.
Application
Registration:
To use the program, you need to register an account. This is a one-time process.
Launch the program and click on the "Registration" button.
Fill in the required registration details such as username(e-mail), password.
Click theRegistration button to create an account.
After registration, you can continue to sign in with your credentials.
Login:
Once registered, you can log in to the app using your account credentials.
Launch the program and click on the "Sign in" button.
Enter your username and password in the appropriate fields.
Click the Sign In button to access your account and app features.
Overview Screen(MainScreen):
Upon logging into the program. At the top you will be able to see NavHost with which you can switch between different screens, you will be presented with the Overview screen, where you can see the exchange rate of the dollar and the euro against the hryvnia (which we connected using the Privat24 API: https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=11), a window for adding a transaction to a Accounts(incomes), Bills, Deposits, Credits. There will also be Cards displaying the last three operations of each of the Screens (Accounts(incomes), Bills, Deposits, Credits)
When switching to any other screen, you will see a drop-down menu with a selection of the type of sorting and all transactions of this type that have been carried out. Also, when you click on any transaction, SingleScreen will open, where you can edit it or delete it altogether.



