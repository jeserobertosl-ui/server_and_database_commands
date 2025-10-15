package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Article
{
    Integer m_id;
    String m_title;
    String m_authors;
    Integer m_publication_date;
    String m_link;
    Integer m_cited_by;

    Article()
    {
        m_id = 0;
        m_title = "";
        m_authors = "";
        m_publication_date = 0;
        m_link = "";
        m_cited_by = 0;
    }

    Article(JsonObject _json_article)
    {
        m_id = 0;
        m_title = _json_article.get("title").getAsString();
        m_authors = _json_article.get("authors").getAsString();
        m_publication_date = _json_article.get("year").getAsInt();
        m_link = _json_article.get("link").getAsString();
        m_cited_by = _json_article.get("cited_by").getAsJsonObject().get("value").getAsInt();
    }

    Article(ResultSet _rs) throws SQLException
    {
        m_id = _rs.getInt("id");
        m_title = _rs.getString("title");
        m_authors = _rs.getString("authors");
        m_publication_date = _rs.getInt("publication_date");
        m_link = _rs.getString("link");
        m_cited_by = _rs.getInt("cited_by");
    }

    static List<Article> extract_articles(JsonArray _articles_arr)
    {
        List<Article> articles_list = new ArrayList<>();

        if (_articles_arr.isEmpty())
        {
            System.out.println("Empty JSON articles array");
            return articles_list;
        }

        for (var i : _articles_arr)
        {
            JsonObject article_info = i.getAsJsonObject();
            Article article = new Article(article_info);
            articles_list.add(article);
        }

        return articles_list;
    }

    static void print_articles(List<Article> _articles)
    {
        for (int i = 0; i < _articles.size(); i++)
        {
            System.out.println
            (
                i + ".- " + "title: " + _articles.get(i).m_title + ", authors: " + _articles.get(i).m_authors
            );
        }
    }

    static JsonArray articles_as_jsonArray(String _content)
    {
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(_content, JsonElement.class);

        return element.getAsJsonObject().getAsJsonArray("articles");
    }

    static List<Article> get_articles_from_author_id(String _author_id) throws SerpApiSearchException
    {
        Map<String, String> parameter = new HashMap<>();

        parameter.put("engine", "google_scholar_author");
        parameter.put("api_key", "327cb56845769584f143324cc5e6143e5c0c493d395bc8cfc665593c59381785");
        parameter.put("author_id", _author_id);

        SerpApiHttpClient client = new SerpApiHttpClient("/search");

        JsonArray articles_results = Article.articles_as_jsonArray(client.getResults(parameter));
        return Article.extract_articles(articles_results);
    }
}
