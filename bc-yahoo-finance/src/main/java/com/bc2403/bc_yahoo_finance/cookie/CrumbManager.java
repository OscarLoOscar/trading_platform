// package com.bc2403.bc_yahoo_finance.cookie;

// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.net.HttpURLConnection;
// import java.net.URI;
// import java.net.URL;
// import java.util.ArrayList;
// import java.util.List;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// public class CrumbManager {

//   private final static Logger log = LoggerFactory.getLogger(CrumbManager.class);

//   public static String crumb = null;
//   public static String cookie = null;

//   private static void setCookie() {
//     try {
//       URL url = new URI("https://fc.yahoo.com?p=us").toURL();
//       HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//       // Get the cookie from the response headers
//       List<String> result = new ArrayList<>();
//       cookie = connection.getHeaderField("Set-Cookie");
//       result.add(cookie);
//       String[] cookies = connection.getHeaderField("Set-Cookie").split(";");
//       cookie = cookies[0];
//       log.info("cookie: {}", cookie);
//       connection.disconnect();
//     } catch (Exception e) {
//       log.debug(
//           "Failed to set cookie from http request. In trade day quote requests will most likely fail.",
//           e);
//     }
//   }

//   public static final String USER_AGENT =
//   "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.42";

//   private static void setCrumb() {
//     StringBuilder response = new StringBuilder();

//     try {
//       URL url =
//           new URI("https://query2.finance.yahoo.com/v1/test/getcrumb").toURL();
//       HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//       // Set the cookie
//       connection.setRequestProperty("Cookie", cookie);
//       connection.addRequestProperty("User-Agent", USER_AGENT);

//       // Make the HTTP request
//       connection.setRequestMethod("GET");

//       // Read the response content
//       try (BufferedReader reader = new BufferedReader(
//           new InputStreamReader(connection.getInputStream()))) {
//         String line;
//         while ((line = reader.readLine()) != null) {
//           response.append(line);
//         }
//       }
//     } catch (Exception e) {
//       log.debug(
//           "Failed to set crumb from http request. In trade day quote requests will most likely fail.",
//           e);
//     }
//     crumb = response.toString();
//   }

//   public static synchronized void resetCookieCrumb() {
//     setCookie();
//     setCrumb();
//   }

//   public static synchronized String getCookie() {
//     if (cookie == null || cookie.isEmpty()) {
//       resetCookieCrumb();
//     }
//     return cookie;
//   }

//   public static synchronized String getCrumb() {
//     if (crumb == null || crumb.isBlank()) {
//       resetCookieCrumb();
//     }
//     return crumb;

//   }

// }
