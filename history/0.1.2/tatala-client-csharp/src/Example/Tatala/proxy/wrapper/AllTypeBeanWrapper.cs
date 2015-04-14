using Example.tatala.model;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.to;
using QiLeYuan.Tatala.socket.util;

namespace com.qileyuan.tatala.example.proxy.wrapper {

    public class AllTypeBeanWrapper : TransferObjectWrapper {

        private AllTypeBean allTypeBean;

        public AllTypeBeanWrapper() {
        }

        public AllTypeBeanWrapper(AllTypeBean allTypeBean) {
            this.allTypeBean = allTypeBean;
        }

        public AllTypeBean getAllTypeBean() {
            return allTypeBean;
        }

        public void setAllTypeBean(AllTypeBean allTypeBean) {
            this.allTypeBean = allTypeBean;
        }

        public int getLength() {
            return TransferUtil.getLengthOfBoolean() +
                   TransferUtil.getLengthOfByte() +
                   TransferUtil.getLengthOfShort() +
                   TransferUtil.getLengthOfChar() +
                   TransferUtil.getLengthOfInt() +
                   TransferUtil.getLengthOfLong() +
                   TransferUtil.getLengthOfFloat() +
                   TransferUtil.getLengthOfDouble() +
                   TransferUtil.getLengthOfDate() +
                   TransferUtil.getLengthOfString(allTypeBean.getAstring());
        }

        public void getByteArray(TransferOutputStream touts) {
            touts.writeBoolean(allTypeBean.isAboolean());
            touts.writeByte(allTypeBean.getAbyte());
            touts.writeShort(allTypeBean.getAshort());
            touts.writeChar(allTypeBean.getAchar());
            touts.writeInt(allTypeBean.getAint());
            touts.writeLong(allTypeBean.getAlong());
            touts.writeFloat(allTypeBean.getAfloat());
            touts.writeDouble(allTypeBean.getAdouble());
            touts.writeDate(allTypeBean.getAdate());
            touts.writeString(allTypeBean.getAstring());
        }

        public TransferObjectWrapper getObjectWrapper(TransferInputStream tins) {
            allTypeBean = new AllTypeBean();
            allTypeBean.setAboolean(tins.readBoolean());
            allTypeBean.setAbyte(tins.readByte());
            allTypeBean.setAshort(tins.readShort());
            allTypeBean.setAchar(tins.readChar());
            allTypeBean.setAint(tins.readInt());
            allTypeBean.setAlong(tins.readLong());
            allTypeBean.setAfloat(tins.readFloat());
            allTypeBean.setAdouble(tins.readDouble());
            allTypeBean.setAdate(tins.readDate());
            allTypeBean.setAstring(tins.readString());
            return this;
        }

    }
}