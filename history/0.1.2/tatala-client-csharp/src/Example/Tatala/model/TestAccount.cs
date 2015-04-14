using System;

/**
 * This class is a customization object.
 * @author JimT
 *
 */
namespace Example.tatala.model {

    public class TestAccount {

        private int id;
        private String name;
        private String address;

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }

        public override String ToString() {
            return "[id: " + id + "][name: " + name + "][address: " + address + "]";
        }
    }
}
