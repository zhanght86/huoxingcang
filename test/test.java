
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

class test{
    public static void main(String[] args) throws MalformedURLException
    {
          ResourceBundle bundle ;
            File file = new File("D:\\web\\msa\\web\\resources\\languages");
            URL url = file.toURI().toURL();
           ClassLoader loader =  new URLClassLoader(new URL[]{url});
           bundle =  ResourceBundle.getBundle("vtl.vtl_root", Locale.FRENCH, loader);
           System.out.println(bundle.getString("ok"));
    }

}