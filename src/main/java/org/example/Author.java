package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Author
{
    Integer m_id;
    String m_name;
    String m_link;
    String m_author_id;
    String m_email;
    Integer m_cited_by;

    Author()
    {
        m_id = 0;
        m_name = "";
        m_link = "";
        m_author_id = "";
        m_email = "";
        m_cited_by = 0;
    }

    Author(JsonObject _author)
    {
        m_id = 0;
        m_name = _author.get("name").getAsString();
        m_link = _author.get("link").getAsString();
        m_author_id = _author.get("author_id").getAsString();
        m_email = _author.get("email").getAsString();
        m_cited_by = _author.get("cited_by").getAsInt();
    }

    Author(ResultSet _rs) throws SQLException
    {
        m_id = _rs.getInt("id");
        m_name = _rs.getString("name");
        m_link = _rs.getString("link");
        m_author_id = _rs.getString("author_id");
        m_email = _rs.getString("email");
        m_cited_by = _rs.getInt("cited_by");
    }

    static List<Author> extract_authors(JsonArray _authors_arr)
    {
        List<Author> author_list = new ArrayList<>();

        for (var i : _authors_arr)
        {
            JsonObject author_info = i.getAsJsonObject();
            Author author = new Author(author_info);
            author_list.add(author);
        }

        return author_list;
    }

    static void print_authors(List<Author> _authors)
    {
        for (int i = 0; i < _authors.size(); i++)
        {
            System.out.println
            (
                i + ".- " + "name: " + _authors.get(i).m_name + ", id: " + _authors.get(i).m_author_id
            );
        }
    }

    static JsonArray authors_as_jsonArray(String _content)
    {
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(_content, JsonElement.class);

        if (element.isJsonNull())
        {
            System.out.println("Could not find authors");
            return new JsonArray();
        }

        return element.getAsJsonObject().get("profiles").getAsJsonObject().getAsJsonArray("authors");
    }

    static Author search_author() throws SerpApiSearchException
    {
        Scanner scnr = new Scanner(System.in);

        System.out.println("Enter the name of the author: ");
        String author_name = scnr.nextLine();

        if (author_name.isEmpty())
        {
            System.out.println("No author name input");
            return new Author();
        }

        Map<String, String> parameter = new HashMap<>();

        parameter.put("engine", "google_scholar");
        parameter.put("api_key", "327cb56845769584f143324cc5e6143e5c0c493d395bc8cfc665593c59381785");
        parameter.put("q", author_name);

        SerpApiHttpClient client = new SerpApiHttpClient("/search");

        JsonArray author_results = Author.authors_as_jsonArray(client.getResults(parameter));
        List<Author> author_list = Author.extract_authors(author_results);

        System.out.println("Select an author:");
        Author.print_authors(author_list);

        Integer option = scnr.nextInt();
        return author_list.get(option);
    }
}
