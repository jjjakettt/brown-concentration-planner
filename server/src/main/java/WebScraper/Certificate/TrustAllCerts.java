package WebScraper.Certificate;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;

public class TrustAllCerts {

  public TrustAllCerts() {
    try {
      trustAllCertificates();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    } catch (KeyManagementException e) {
      throw new RuntimeException(e);
    }
  }
  /**
   * method to bypass SSL certificates
   *
   * @throws NoSuchAlgorithmException
   * @throws KeyManagementException
   */
  private static void trustAllCertificates()
      throws NoSuchAlgorithmException, KeyManagementException {
    TrustManager[] trustAllCerts =
        new TrustManager[] {
          new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
              // returning null here means that all certificates will be trusted bc this usually
              // returns the trusted certificates
              return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
              // usually checks if a client's certificate is trusted, by leaving this empty all will
              // be trusted
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
              // usually checks if the server certificate is trusted, by leaving this empty all will
              // be trusted
            }
          }
        };

    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
  }
}
