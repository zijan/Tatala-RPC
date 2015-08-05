using System;

namespace Example.tatala.model {

    public class AllTypeBean {
        bool aboolean;
        byte abyte;
        short ashort;
        char achar;
        int aint;
        long along;
        float afloat;
        double adouble;
        DateTime adate;
        String astring;

        public bool isAboolean() {
            return aboolean;
        }
        public void setAboolean(bool aboolean) {
            this.aboolean = aboolean;
        }
        public byte getAbyte() {
            return abyte;
        }
        public void setAbyte(byte abyte) {
            this.abyte = abyte;
        }
        public short getAshort() {
            return ashort;
        }
        public void setAshort(short ashort) {
            this.ashort = ashort;
        }
        public char getAchar() {
            return achar;
        }
        public void setAchar(char achar) {
            this.achar = achar;
        }
        public int getAint() {
            return aint;
        }
        public void setAint(int aint) {
            this.aint = aint;
        }
        public long getAlong() {
            return along;
        }
        public void setAlong(long along) {
            this.along = along;
        }
        public float getAfloat() {
            return afloat;
        }
        public void setAfloat(float afloat) {
            this.afloat = afloat;
        }
        public double getAdouble() {
            return adouble;
        }
        public void setAdouble(double adouble) {
            this.adouble = adouble;
        }
        public DateTime getAdate() {
            return adate;
        }
        public void setAdate(DateTime adate) {
            this.adate = adate;
        }
        public String getAstring() {
            return astring;
        }
        public void setAstring(String astring) {
            this.astring = astring;
        }


        public override String ToString() {
            String TAB = "    ";
            String retValue = "";
            retValue = "AllTypeBean ( "
                + "aboolean = " + this.aboolean + TAB
                + "abyte = " + this.abyte + TAB
                + "ashort = " + this.ashort + TAB
                + "achar = " + this.achar + TAB
                + "aint = " + this.aint + TAB
                + "along = " + this.along + TAB
                + "afloat = " + this.afloat + TAB
                + "adouble = " + this.adouble + TAB
                + "adate = " + this.adate + TAB
                + "astring = " + this.astring + TAB
                + " )";
            return retValue;
        }
    }
}