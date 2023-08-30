package android.hardware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 30.08.2023, 13:46
 */
public interface ISensorPrivacyManager extends IInterface {
    void addSensorPrivacyListener(ISensorPrivacyListener iSensorPrivacyListener) throws RemoteException;

    boolean isSensorPrivacyEnabled() throws RemoteException;

    void removeSensorPrivacyListener(ISensorPrivacyListener iSensorPrivacyListener) throws RemoteException;

    void setSensorPrivacy(boolean z) throws RemoteException;

    class Default implements ISensorPrivacyManager {
        public void addSensorPrivacyListener(ISensorPrivacyListener listener) throws RemoteException {

        }

        public void removeSensorPrivacyListener(ISensorPrivacyListener listener) throws RemoteException {

        }

        public boolean isSensorPrivacyEnabled() throws RemoteException {
            return false;
        }

        public void setSensorPrivacy(boolean enable) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    abstract class Stub extends Binder implements ISensorPrivacyManager {
        private static final String DESCRIPTOR = "android.hardware.ISensorPrivacyManager";
        static final int TRANSACTION_addSensorPrivacyListener = 1;
        static final int TRANSACTION_isSensorPrivacyEnabled = 3;
        static final int TRANSACTION_removeSensorPrivacyListener = 2;
        static final int TRANSACTION_setSensorPrivacy = 4;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISensorPrivacyManager asInterface(IBinder iBinder) {
            if (iBinder == null)
                return null;
            IInterface anInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (!(anInterface instanceof ISensorPrivacyManager))
                return new Proxy(iBinder);
            return (ISensorPrivacyManager) anInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case TRANSACTION_addSensorPrivacyListener:
                    return "addSensorPrivacyListener";
                case TRANSACTION_removeSensorPrivacyListener:
                    return "removeSensorPrivacyListener";
                case TRANSACTION_isSensorPrivacyEnabled:
                    return "isSensorPrivacyEnabled";
                case TRANSACTION_setSensorPrivacy:
                    return "setSensorPrivacy";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, @NonNull Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1598968902) {
                switch (code) {
                    case TRANSACTION_addSensorPrivacyListener:
                        data.enforceInterface(DESCRIPTOR);
                        addSensorPrivacyListener(ISensorPrivacyListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case TRANSACTION_removeSensorPrivacyListener:
                        data.enforceInterface(DESCRIPTOR);
                        removeSensorPrivacyListener(ISensorPrivacyListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case TRANSACTION_isSensorPrivacyEnabled:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result = isSensorPrivacyEnabled();
                        reply.writeNoException();
                        reply.writeInt(_result ? 1 : 0);
                        return true;
                    case TRANSACTION_setSensorPrivacy:
                        data.enforceInterface(DESCRIPTOR);
                        setSensorPrivacy(data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements ISensorPrivacyManager {
            public static ISensorPrivacyManager sDefaultImpl;
            private final IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void addSensorPrivacyListener(ISensorPrivacyListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addSensorPrivacyListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeSensorPrivacyListener(ISensorPrivacyListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeSensorPrivacyListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSensorPrivacyEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSensorPrivacyEnabled();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status = z;
                    _reply.recycle();
                    _data.recycle();
                    return _status;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSensorPrivacy(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSensorPrivacy(enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISensorPrivacyManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISensorPrivacyManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}