<!DOCTYPE html>
<html>
<head>
</head>
<body>
  <h1>CocoBank</h1>
  <h2>Introduction</h2>
  <p>
    This program is designed to allow users to track expenses, make deposits, and perform other financial operations. It is developed using Kotlin (Android Compose), an object-oriented approach that enables a modular and interactive program structure. The backend technologies used include Spring Boot, Spring Security, SQL, and a server on Microsoft Azure. The user interface is developed in English, providing a user-friendly and intuitive experience. Whether you are an individual managing personal finances or a small business in need of a reliable financial management solution, CocoBank is designed to meet your needs. This documentation provides detailed instructions on installation, configuration, and utilization of the program, along with code examples and troubleshooting information.
  </p>
  <h2>Requirements</h2>
  <p>
    Minimum system requirements for the program:
  </p>
  <ul>
    <li>Operating System: Android version 5.0 or higher</li>
    <li>Memory: Minimum of 2 GB RAM</li>
    <li>Hard Disk Space: Minimum of 50 MB for program installation</li>
    <li>Other Requirements: High-speed internet connection for certain features or online functionality, availability of the latest drivers and updates for the operating system and hardware components</li>
  </ul>
  <p>
    Please note that these are minimum requirements, and actual requirements may vary depending on the size of the program, its features, and other factors. It is also recommended to have up-to-date operating system updates and drivers for optimal program performance.
  </p>
  <h2>Installation</h2>
  <p>
    To install and run the program, please follow these steps:
  </p>
  <ol>
    <li>Download the program folder from the provided source.</li>
    <li>Locate the downloaded folder on your computer.</li>
    <li>Open the folder and look for the executable (.exe) file.</li>
    <li>Double-click the executable file to launch the program.</li>
  </ol>
  <p>
    By executing the .exe file, the program will be launched, and you will be able to access its features and functionalities. It's important to ensure that you have downloaded the complete program folder and have the necessary permissions to run executable files on your computer.
  </p>
  <p>
    Note: In some cases, you might need to extract the downloaded folder from a compressed archive (e.g., a ZIP file) before being able to access the .exe file. In such cases, use a file extraction tool (such as WinRAR, 7-Zip, or the built-in extraction utility in your operating system) to extract the folder before running the executable file.
  </p>
  <h2>Application</h2>
  <h3>Registration:</h3>
  <p>
    To use the program, you need to register an account. This is a one-time process.
  </p>
  <ol>
    <li>Launch the program and click on the "Registration" button.</li>
    <li>Fill in the required registration details such as username (e-mail) and password.</li>
    <li>Click the "Registration" button to create an account.</li>
  </ol>
  <p>
    After registration, you can continue to sign in with your credentials.
  </p>
  <h3>Login:</h3>
  <p>
    Once registered, you can log in to the app using your account credentials.
  </p>
  <ol>
    <li>Launch the program and click on the "Sign in" button.</li>
    <li>Enter your username and password in the appropriate fields.</li>
    <li>Click the "Sign In" button to access your account and app features.</li>
  </ol>
  <h3>Overview Screen (MainScreen):</h3>
  <p>
    Upon logging into the program, you will be presented with the Overview screen. At the top, you will see NavHost, which allows you to switch between different screens. On the Overview screen, you can see the exchange rate of the dollar and the euro against the hryvnia (connected using the Privat24 API: <a href="https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=11">https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=11</a>). There is also a window for adding transactions to Accounts (incomes), Bills, Deposits, and Credits. Additionally, there are cards displaying the last three operations of each screen (Accounts, Bills, Deposits, Credits). When switching to any other screen, you will see a drop-down menu with sorting options and all transactions of that type. Clicking on a transaction will open the SingleScreen, where you can edit or delete it.
  </p>
</body>
</html>
