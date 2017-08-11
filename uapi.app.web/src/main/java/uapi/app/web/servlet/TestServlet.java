package uapi.app.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xquan on 5/24/2017.
 */
@WebServlet(name="AnnotationServlet", urlPatterns="/AnnotationServlet")
public class TestServlet extends HttpServlet {

    @Override
    public void doGet(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML>");
        out.println("<HTML>");
        out.println("   <HEAD>");
        out.println("       <TITLE>A Servlet</TITLE>");
        out.println("       <meta http-equiv=\"content-type\" " + "content=\"text/html; charset=utf-8\">");
        out.println("   </HEAD>");
        out.println("   <BODY>");
        out.println("       Hello AnnotationServlet.");
        out.println("   </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }
}
