// import java.io.File;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.PrintWriter;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.util.ArrayList;
// import java.util.List;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.MultipartConfig;
// import jakarta.servlet.annotation.WebServlet;
// import jakarta.servlet.http.HttpServlet;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.servlet.http.Part;

// @WebServlet("/notesManager")
// @MultipartConfig(fileSizeThreshold = 1024*1024*1, maxFileSize = 1024*1024*20, maxRequestSize = 1024*1024*25)
// public class NotesManagerServlet extends HttpServlet {
//     private static final long serialVersionUID = 1L;

//     private static final String DB_URL = "jdbc:mysql://localhost:3306/digital_notes";
//     private static final String DB_USER = "root";
//     private static final String DB_PASS = "password"; // Change to your MySQL password

//     private static final String UPLOAD_DIR = "uploads";

//     // Handle Teacher Uploads
//     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//         String teacherName = request.getParameter("teacherName");
//         String teacherEmail = request.getParameter("teacherEmail");
//         String subject = request.getParameter("subject");
//         String title = request.getParameter("noteTitle");
//         Part filePart = request.getPart("noteFile");

//         // Save uploaded file on server
//         String fileName = filePart.getSubmittedFileName();
//         String appPath = request.getServletContext().getRealPath("");
//         String uploadPath = appPath + File.separator + UPLOAD_DIR;

//         File uploadDir = new File(uploadPath);
//         if(!uploadDir.exists()) uploadDir.mkdir();

//         try (InputStream is = filePart.getInputStream();
//              FileOutputStream fos = new FileOutputStream(uploadPath + File.separator + fileName)) {
//             byte[] buffer = new byte[1024];
//             int bytesRead;
//             while((bytesRead = is.read(buffer)) != -1){
//                 fos.write(buffer, 0, bytesRead);
//             }
//         }

//         // Store metadata in MySQL
//         try {
//             Class.forName("com.mysql.cj.jdbc.Driver");
//             Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
//             String sql = "INSERT INTO notes (title, subject, teacher_name, file_name) VALUES (?,?,?,?)";
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ps.setString(1, title);
//             ps.setString(2, subject);
//             ps.setString(3, teacherName);
//             ps.setString(4, fileName);
//             ps.executeUpdate();
//             conn.close();
//         } catch (Exception e) {
//             e.printStackTrace();
//             response.setStatus(500);
//             response.getWriter().print("Database error!");
//             return;
//         }

//         response.setContentType("text/plain");
//         response.getWriter().print("Note uploaded successfully!");
//     }

//     // Handle Student Fetch Notes
//     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//         String subjectFilter = request.getParameter("subject");
//         List<String[]> notesList = new ArrayList<>();

//         try {
//             Class.forName("com.mysql.cj.jdbc.Driver");
//             Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

//             String sql = "SELECT title, teacher_name, subject, file_name FROM notes";
//             if(subjectFilter != null && !subjectFilter.isEmpty()){
//                 sql += " WHERE subject=?";
//             }

//             PreparedStatement ps = conn.prepareStatement(sql);
//             if(subjectFilter != null && !subjectFilter.isEmpty()) ps.setString(1, subjectFilter);
//             ResultSet rs = ps.executeQuery();

//             while(rs.next()){
//                 notesList.add(new String[]{
//                     rs.getString("title"),
//                     rs.getString("teacher_name"),
//                     rs.getString("subject"),
//                     rs.getString("file_name")
//                 });
//             }
//             conn.close();
//         } catch (Exception e) {
//             e.printStackTrace();
//             response.setStatus(500);
//             response.getWriter().print("Database error!");
//             return;
//         }

//         // Return notes as JSON
//         response.setContentType("application/json");
//         PrintWriter out = response.getWriter();
//         out.print("[");
//         for(int i=0; i<notesList.size(); i++){
//             String[] n = notesList.get(i);
//             out.print("{\"title\":\""+n[0]+"\",\"teacher\":\""+n[1]+"\",\"subject\":\""+n[2]+"\",\"file\":\""+n[3]+"\"}");
//             if(i != notesList.size()-1) out.print(",");
//         }
//         out.print("]");
//         out.close();
//     }
// }
