using System;
using System.Threading;
using Example.tatala.model;
using Example.tatala.proxy;
using QiLeYuan.Tools.debug;

namespace Example.tatala.client {
    /// <summary>
    /// This class is a sample for c# client.
    /// @author JimT
    /// 
    /// Long Connect Remote test, client and server at same machine. (n=numThread; t=times; e=error)
    /// CPU: i7-3610QM; RAM: 8G; OS: Win7-64
    /// n(100) t(100) e(0) time: 2207(ms) 4531*5/s (first 5 tests method)
    /// </summary>
    public class TestClient {

        static int numThread = 1;//100;
        static int times = 1;//100;
        TestClientProxy manager = new TestClientProxy();

        public static void Main(string[] s) {
            try {

                Logging.AddDebug(new ConsoleDebug());
                Logging.setLevel(Logging.Level.DEBUG);

                System.Diagnostics.Stopwatch stopwatch = new System.Diagnostics.Stopwatch();
                stopwatch.Start();

                TestClient tc = new TestClient();
                tc.MultipleTest();

                stopwatch.Stop();
                TimeSpan timespan = stopwatch.Elapsed;

                Logging.LogInfo("time: " + timespan.TotalMilliseconds + "(ms)");

            } catch (Exception ex) {
                Logging.LogError(ex.ToString());
            }
        }

        public void MultipleTest(){
            for (int i = 0; i < numThread; i++) {
                ThreadStart start = new ThreadStart(testMethod);
                Thread t = new Thread(start);
                t.Start();
                t.Join();
            }
        }

        private void testMethod() {
            for (int i = 0; i < times; i++) {
                RemoteTest();
            }
        }

        public void RemoteTest() {
            try {
                
                //int, String and return String testing
                int Id = 18;
                String name = "JimT";
                String result = manager.sayHello(Id, name);
                Logging.LogDebug("result: " + result);
                
                //no parameter, void return testing
                manager.doSomething();

                //object parameter, object return testing
                TestAccount account = new TestAccount();
                account.setId(1000);
                account.setName("JimT");
                account = manager.getAccount(account);
                Logging.LogDebug(account);

                //all primitive type parameter, object return testing
                AllTypeBean allTypeBean = manager.getAllTypeBean(true, (byte)25, (short)-2, 'T', 3, (long)4, 5.5f, 6.66d, DateTime.Now, "Hello JimT!");
                Logging.LogDebug("allTypeBean: " + allTypeBean);

                //int array and string array, string array return testing
                int[] intarr = new int[3];
                intarr[0] = 1;
                intarr[1] = 2;
                intarr[2] = 3;
                String[] strarr = new String[3];
                strarr[0] = "Jim ";
                strarr[1] = "Tang ";
                strarr[2] = "Toronto ";
                strarr = manager.getArray(intarr, strarr);
                if (strarr != null) {
                    for (int i = 0; i < strarr.Length; i++) {
                        Logging.LogDebug("strarr: " + strarr[i]);
                    }
                }
                
                //below two are not for performance test
                //compress testing
                account = new TestAccount();
                account.setId(1000);
                string str = "";
                for (int i = 0; i < 50; i++) {
                    str += "The quick brown fox jumps over the lazy dog.  ";
                }
                account.setName(str);
                account = manager.getAccountCompress(account);
                Logging.LogDebug(account);


                //asychronous testing
                account = new TestAccount();
                account.setId(1000);
                account.setName("JimT");
                account = manager.getAccountAsynchronous(account);
                Logging.LogDebug(account);
                
            } catch (Exception ex) {
                Logging.LogError(ex.ToString());
            }
        }
    }
}
