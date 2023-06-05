package ru.molokoin.j210.servlets;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.molokoin.j210.services.RepositoryFace;
import ru.molokoin.j210.utils.Markdown;
import ru.molokoin.j210.utils.XMLTransformer;

/**
 * Сервлет, собирающий стартовую страницу
 * 
 */
@WebServlet(name = "Home", value = "/home")
public class Home extends HttpServlet{
    @EJB
    private RepositoryFace repository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        page(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        page(request, response);
    }

    private void page(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
        out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("    <title>j200:view-list</title>");
        /**
         * Подключение стилей
         */
        out.println("    <link href=\"layout/styles.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<html><body >");
        out.println("<header>");
        out.println("   <h1 >J200 : Разработка веб-сервисов</h1>");
        out.println("</header>");
        out.println("<aside>");
        out.println("   <h2 >CONTROLS :</h2>");
        //link j200 : edit base
        out.println("    <a href=\"http://www.molokoin.ru:8080/j200/view-list\">EDIT-BASE : J200</a>");
        //link j210 : Clients.xml
        out.println("    <a href=\"http://www.molokoin.ru:8080/j210/content/Clients.xml\">VIEW : Clients.xml</a>");
        out.println("   <h2 >FILTERS :</h2>");
        //link j210 : CheckSAX
        //link j210 : CheckDOM
        out.println("</aside>");
        out.println("<main>");
        out.println("<div>");

        /**
         * Парсим README.md
         */
        String ROOT_PATH = request.getSession().getServletContext().getRealPath("");
        String path_md = ROOT_PATH + File.separator + "content" + File.separator + "README.md";
        String content_html = Markdown.toHTML(getContent(path_md));
        out.println(content_html);
        out.println("</div>");

        out.println("</main>");
        out.println("<footer>");
        /**
         * Выводим данные о клиентах
         */
        String path_Clients = ROOT_PATH + File.separator + "content" + File.separator + "Clients.xml";
        // String msg = "msg: Тестовый контент 1";
        // putContent(path_test, msg);
        XMLTransformer transformer = new XMLTransformer();
        transformer.createXml(new File(path_Clients), repository);
        out.println("   <h2 >Содержимое сформированного Clients.xml</h2>");
        out.println("<textarea rows=\"20\" cols=\"100\">" + getContent(path_Clients) + "</textarea>");
        // out.println("<xmp>" + getContent(path_Clients) + "</xmp>");
        //out.println(getContent(path_Clients));

        /**
         * Смотрим список файлов и директорий
         */
        out.println("<p>" + "Файлы контента:");
        Set<String> files = listFiles(ROOT_PATH + File.separator + "content");
        for (String file : files) {
            out.println("<p>" + "FILE: " + file);
        }

        /**
         * 
         */


        out.println("</footer>");
        out.println("</body></html>");
    }

    private String getContent(String path){
        StringBuilder sb = new StringBuilder();
        try(FileReader reader = new FileReader(path)){
            // читаем посимвольно
            int c;
            while((c=reader.read())!=-1){
                sb.append((char)c);
                System.out.print((char)c);
            } 
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }   
        return sb.toString();
    }

    private void putContent(String path, String msg){
        try(FileWriter writer = new FileWriter(path, false))
        {
           // запись всей строки
            writer.write(msg);
            // запись по символам
            // writer.append('\n');
            // writer.append('E');
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        } 
    }
    public Set<String> listFiles(String dir) throws IOException {
        Set<String> fileSet = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileSet.add(path.getFileName()
                        .toString());
                }
            }
        }
        return fileSet;
    }
}
