package org.example;

import java.sql.SQLException;
import java.util.*;

public class Main
{
    /**
     * Reads user input for selecting (Add & Delete) operations for (Authors & Articles)
     * @return Integer with the selected option or -1 if something failed
     * @throws SQLException from failed CRUD operations
     * @throws SerpApiSearchException from Google Scholar or Google Scholar Author failed queries
     */
    static Integer menu() throws SQLException, SerpApiSearchException
    {
        System.out.println
        (
            """
            Options:
            0.- Add Researcher
            1.- Delete Researcher
            2.- Add Article
            3.- Delete Article
            4.- View Researchers
            5.- View Articles
            6.- Exit
            Select an option:
            """
        );

        Scanner scnr = new Scanner(System.in);
        Integer option = scnr.nextInt();
        Integer menu_option_copy = option;

        switch(option)
        {
            //Add author
            case 0:
            {
                Researchers_CRUD.s_connection = DatabaseConnection.connect
                        ("articles", "root", "");

                Author author = Author.search_author();

                Researchers_CRUD.create(author);
                DatabaseConnection.close(Researchers_CRUD.s_connection);
            }
            break;

            //Delete author
            case 1:
            {
                Researchers_CRUD.s_connection = DatabaseConnection.connect
                        ("articles", "root", "");

                System.out.println("Select a researcher: ");

                List<Author> author_list = Researchers_CRUD.read();
                Author.print_authors(author_list);
                option = scnr.nextInt();

                Researchers_CRUD.delete(author_list.get(option).m_author_id);

                DatabaseConnection.close(Researchers_CRUD.s_connection);
            }
            break;

            //Add article
            case 2:
            {
                Researchers_CRUD.s_connection = DatabaseConnection.connect
                        ("articles", "root", "");

                System.out.println("Select an author");
                List<Author> author_list = Researchers_CRUD.read();
                Author.print_authors(author_list);
                option = scnr.nextInt();

                List<Article> articles_list = Article.get_articles_from_author_id(author_list.get(option).m_author_id);
                Article.print_articles(articles_list);

                System.out.println("Select articles from available researchers");
                option = scnr.nextInt();

                Articles_CRUD.s_connection = DatabaseConnection.connect
                        ("articles", "root", "");

                Articles_CRUD.create(articles_list.get(option));
                DatabaseConnection.close(Articles_CRUD.s_connection);

                System.out.println("Article added");
            }
            break;

            //Delete article
            case 3:
            {
                Articles_CRUD.s_connection = DatabaseConnection.connect
                        ("articles", "root", "");

                System.out.println("Select an article: ");

                List<Article> articles_list = Articles_CRUD.read();
                Article.print_articles(articles_list);

                option = scnr.nextInt();

                Articles_CRUD.delete(articles_list.get(option).m_id);
                DatabaseConnection.close(Articles_CRUD.s_connection);
            }
            break;

            //View authors
            case 4:
            {
                Researchers_CRUD.s_connection = DatabaseConnection.connect
                        ("articles", "root", "");

                Author.print_authors(Researchers_CRUD.read());

                DatabaseConnection.close(Researchers_CRUD.s_connection);
            }
            break;

            //View articles
            case 5:
            {
                Articles_CRUD.s_connection = DatabaseConnection.connect
                        ("articles", "root", "");

                Article.print_articles(Articles_CRUD.read());

                DatabaseConnection.close(Articles_CRUD.s_connection);
            }
            break;
        }
        return menu_option_copy;
    }

    /**
     * executes menu function and handles exceptions
     * @param args standard arguments for main function call
     */
    public static void main(String[] args)
    {
        try
        {
            while (menu() != 6);
        }
        catch (SerpApiSearchException ex)
        {
            System.out.println("Exception:");
            System.out.println(ex.getMessage());
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}