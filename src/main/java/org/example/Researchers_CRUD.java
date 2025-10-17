package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * s_connection: must set equal to DatabaseConnection.connect(_database_name, _user, _password).
 * All functions are static and use the previously initialized connection, that is closed by the end of every function
 */
public class Researchers_CRUD
{
    static Connection s_connection;

    static void create(Author _author)
    {
        if (_author.m_author_id.isEmpty())
        {
            System.out.println("Could not add author");
            return;
        }
        try
        {
            if (_author.m_author_id.equals(Researchers_CRUD.get_one(_author.m_author_id).m_author_id))
            {
                System.out.println("Author already exists");
                return;
            }

            String sql = "INSERT INTO researchers(" +
                "name, " +
                "link, " +
                "author_id, " +
                "email, " +
                "cited_by) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement stmt = s_connection.prepareStatement(sql);

            stmt.setString(1, _author.m_name);
            stmt.setString(2, _author.m_link);
            stmt.setString(3, _author.m_author_id);
            stmt.setString(4, _author.m_email);
            stmt.setInt(5, _author.m_cited_by);

            stmt.executeUpdate();
            System.out.println("Researcher added");
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }
    }

    static List<Author> read()
    {
        List<Author> author_list = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM researchers";

            PreparedStatement stmt = s_connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                author_list.add(new Author(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return author_list;
    }

    static Author get_one(String _author_id)
    {
        Author author = new Author();
        try
        {
            String sql = "SELECT * FROM researchers WHERE author_id = ?";

            PreparedStatement stmt = s_connection.prepareStatement(sql);

            stmt.setString(1, _author_id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return new Author(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return author;
    }

    static void update(Author _author)
    {
        String sql = "UPDATE researchers SET " +
            "name = ?, " +
            "link = ?, " +
            "email = ?, " +
            "cited_by = ? WHERE author_id = ?";

        try
        {
            PreparedStatement stmt = s_connection.prepareStatement(sql);

            stmt.setString(1, _author.m_name);
            stmt.setString(2, _author.m_link);
            stmt.setString(3, _author.m_author_id);
            stmt.setString(4, _author.m_email);
            stmt.setInt(5, _author.m_cited_by);

            stmt.setString(6, _author.m_author_id);


            stmt.executeUpdate();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    static void delete(String _author_id)
    {
        if (_author_id.isEmpty())
        {
            System.out.println("Author id not provided for deletion");
            return;
        }

        String sql = "DELETE FROM researchers WHERE author_id = ?";

        try
        {
            PreparedStatement stmt = s_connection.prepareStatement(sql);

            stmt.setString(1, _author_id);

            stmt.executeUpdate();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
