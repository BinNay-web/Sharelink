import { NativeModules, NativeEventEmitter, EmitterSubscription } from 'react-native';

const { WifiDirectModule } = NativeModules;
const eventEmitter = new NativeEventEmitter(WifiDirectModule);

export interface Peer {
  deviceName: string;
  deviceAddress: string;
}

export interface ConnectionInfo {
  groupOwnerAddress: string;
  isGroupOwner: boolean;
}

export const WifiDirect = {
  initialize: (): Promise<boolean> => WifiDirectModule.initialize(),

  createGroup: (): Promise<boolean> => WifiDirectModule.createGroup(),

  removeGroup: (): Promise<boolean> => WifiDirectModule.removeGroup(),

  discoverPeers: (): Promise<boolean> => WifiDirectModule.discoverPeers(),

  connectToPeer: (deviceAddress: string): Promise<boolean> =>
    WifiDirectModule.connectToPeer(deviceAddress),

  onPeersChanged: (callback: (peers: Peer[]) => void): EmitterSubscription =>
    eventEmitter.addListener('onPeersChanged', callback),

  onConnectionChanged: (callback: (info: ConnectionInfo) => void): EmitterSubscription =>
    eventEmitter.addListener('onConnectionChanged', callback),
};