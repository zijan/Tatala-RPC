using QiLeYuan.Tatala.socket.io;

/**
 * This is customization object wrapper interface.
 * 
 * @author JimT
 *
 */
namespace QiLeYuan.Tatala.socket.to {
    public interface TransferObjectWrapper {
        int getLength();
        void getByteArray(TransferOutputStream touts);
        TransferObjectWrapper getObjectWrapper(TransferInputStream tins);
    }
}
