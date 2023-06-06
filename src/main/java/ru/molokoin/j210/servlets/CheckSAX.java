package ru.molokoin.j210.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.molokoin.j210.entities.Address;
import ru.molokoin.j210.entities.Client;
import ru.molokoin.j210.services.RepositoryFace;
import ru.molokoin.j210.utils.DemoSAX;

@WebServlet(name = "CheckSAX", value = "/check-sax")
public class CheckSAX extends HttpServlet{
    @EJB
    private RepositoryFace repository;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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
        out.println("   <h1 >J210 : Разработка веб-сервисов</h1>");
        out.println("</header>");
        out.println("<aside>");
        out.println("   <h2 >CONTROLS :</h2>");
        //возврат на домашнюю страницу
        out.println("    <a href=\"home\">HOME : J210</a>");
        out.println("</aside>");
        out.println("<main>");
        out.println("<div>");
        /**
         * Выводим данные о клиентах
         */
        String ROOT_PATH = request.getSession().getServletContext().getRealPath("");
        String path_Clients = ROOT_PATH + File.separator + "content" + File.separator + "Clients.xml";
        File xml = new File(path_Clients);
        Collection<Client> clients = new ArrayList<>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            DemoSAX sax = new DemoSAX(request.getParameter("filter-name"), xml, repository);
            parser.parse(xml, sax);
            clients = sax.read();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        out.println("   <h2 >Данные полученные методом SAX из файла Clients.xml (фильтр: "+ request.getParameter("filter-name") +")</h2>");
        if (clients.size()>0){
            out.println("<br><br>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<td>ID</td>");
            out.println("<td>ФИО</td>");
            out.println("<td>Тип клиента</td>");
            out.println("<td>Дата добавления</td>");
            out.println("<td>Сведения об адресах</td>");
            out.println("</tr>");
            for(Client client : clients) {
                    out.println("<tr>");
                    out.println("<td>" + client.getId() +"</td>");
                    
                    out.println("<td>" + "<a href=\"../j200/create-client?clientid="+client.getId()+"\">"+ client.getName()  +"</a></td>");
                    out.println("<td>" + client.getClient_type() + "</td>");
                    out.println("<td>" + client.getAdded() + "</td>");
                    out.println("<td>");
                    for(Address address : client.getAddresses()){
                        out.println("<a href=\"../j200/add-address?addressid=" + address.getId() + "\">");
                        out.println(address.toString());
                        out.println("</a>");
                        out.println("<br>");
                        out.println("<br>");
                    }
                    out.println("</td>");
                    out.println("</tr>");
            }
            out.println("</table>");
        }else{
            out.println("<br><br>");
            out.println("   <p >Фильтр вернул пустую выборку, попробуйте уточнить параметры фильтрации ...</p>");
            out.println("<br><br>");
            out.println("    <a href=\"home\">Вернуться к домашней странице приложения</a>");
        }
        out.println("</div>");
        out.println("</main>");
        out.println("</body></html>");
    }
    
}
