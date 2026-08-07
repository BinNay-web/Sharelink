# ShareLink Wi-Fi Direct Project

## Overview
The ShareLink Wi-Fi Direct Project is a React Native application that enables peer-to-peer connections between devices using Wi-Fi Direct technology. This project provides a seamless way to discover, connect, and communicate with nearby devices without the need for an internet connection.

## Features
- Initialize Wi-Fi Direct functionality
- Create and remove Wi-Fi Direct groups
- Discover nearby devices
- Connect to peers
- Listen for changes in peer availability and connection status

## Project Structure
```
sharelink-wifi-direct-project
├── android
│   ├── app
│   │   └── src
│   │       └── main
│   │           └── java
│   │               └── com
│   │                   └── sharelinkapp
│   │                       └── wifidirect
│   │                           ├── WifiDirectModule.kt
│   │                           ├── WifiDirectPackage.kt
│   │                           └── WifiDirectBroadcastReceiver.kt
│   ├── build.gradle
│   ├── settings.gradle
│   └── gradle.properties
├── src
│   └── WifiDirect.ts
├── package.json
├── tsconfig.json
└── README.md
```

## Installation
1. Clone the repository:
   ```
   git clone <repository-url>
   cd sharelink-wifi-direct-project
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. For Android, ensure you have the necessary SDKs and tools installed. Open the project in Android Studio and sync the Gradle files.

## Usage
1. Import the `WifiDirect` module in your React Native components:
   ```javascript
   import { WifiDirect } from './src/WifiDirect';
   ```

2. Initialize the module:
   ```javascript
   WifiDirect.initialize()
     .then(success => {
       if (success) {
         console.log('Wi-Fi Direct initialized successfully');
       }
     })
     .catch(error => {
       console.error('Initialization failed:', error);
     });
   ```

3. Use other methods to create groups, discover peers, and connect to devices as needed.

## Contributing
Contributions are welcome! Please submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.