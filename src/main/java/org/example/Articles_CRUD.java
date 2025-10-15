package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Articles_CRUD
{
    static Connection s_connection;

    static void create(Article _article)
    {
        try
        {
            String sql = "INSERT INTO articles(" +
                "title, " +
                "authors, " +
                "publication_date, " +
                "link, " +
                "cited_by) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement stmt = s_connection.prepareStatement(sql);

            stmt.setString(1, _article.m_title);
            stmt.setString(2, _article.m_authors);
            stmt.setInt(3, _article.m_publication_date);
            stmt.setString(4, _article.m_link);
            stmt.setInt(5, _article.m_cited_by);

            stmt.executeUpdate();
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }
    }

    static List<Article> read()
    {
        List<Article> articles_list = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM articles";

            PreparedStatement stmt = s_connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                articles_list.add(new Article(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return articles_list;
    }

    static Article get_one(Integer _id)
    {
        try
        {
            String sql = "SELECT * FROM articles WHERE id = ?";

            PreparedStatement stmt = s_connection.prepareStatement(sql);

            stmt.setInt(1, _id);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return new Article(rs);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return new Article();
        }
    }

    static void update(Integer _id, Article _article)
    {
        String sql = "UPDATE articles SET " +
                "title = ?, " +
                "authors = ?, " +
                "publication_date = ?, " +
                "link = ?, " +
                "cited_by = ? WHERE id = ?";

        try
        {
            PreparedStatement stmt = s_connection.prepareStatement(sql);

            stmt.setString(1, _article.m_title);
            stmt.setString(2, _article.m_authors);
            stmt.setInt(3, _article.m_publication_date);
            stmt.setString(4, _article.m_link);
            stmt.setInt(5, _article.m_cited_by);

            stmt.setInt(6, _id);


            stmt.executeUpdate();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    static void delete(Integer _id)
    {
        String sql = "DELETE FROM articles WHERE id = ?";

        try
        {

            PreparedStatement stmt = s_connection.prepareStatement(sql);

            stmt.setInt(1, _id);

            stmt.executeUpdate();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
