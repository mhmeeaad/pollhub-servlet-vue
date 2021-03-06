/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import model.Poll;
import model.Question;
import model.connection;

/**
 *
 * @author y
 */
public class PollCrud {

    connection conn = new connection();

    public void add(String title, int user, boolean aissuspended, boolean uissuspended, boolean close, ArrayList<Question> questions) throws SQLException {

        try (Connection c = conn.connect(); PreparedStatement add = c.prepareStatement("INSERT INTO poll (title, user, aissuspended, uissuspended, close) VALUES (?, ?, ?, ?, ?)")) {
            add.setString(1, title);
            add.setInt(2, user);
            add.setBoolean(3, aissuspended);
            add.setBoolean(4, uissuspended);
            add.setBoolean(5, close);

            add.executeUpdate();
            add.close();
            c.close();
            ArrayList<Poll> polls = (ArrayList<Poll>) selectall();
            int poll;
            poll = polls.get(polls.size() - 1).pollid;
            crud.QuestionCrud question = new crud.QuestionCrud();
            for (int i = 0; i < questions.size(); i++) {
                questions.get(i).poll = poll;

            }
            question.addQuestions(questions);

        }

        System.out.println("Insert is done successfully");

    }

    public void update(String title, int pollid, int user, boolean aissuspended, boolean uissuspended, boolean close, ArrayList<Question> questions) throws SQLException {
        try (Connection c = conn.connect(); PreparedStatement add = c.prepareStatement("INSERT INTO poll (title, user, aissuspended, uissuspended, close) VALUES (?, ?, ?, ?, ?)")) {
            add.setString(1, title);
            add.setInt(2, user);
            add.setBoolean(3, aissuspended);
            add.setBoolean(4, uissuspended);
            add.setBoolean(5, close);

            add.executeUpdate();
            add.close();
            c.close();
            
            crud.QuestionCrud question = new crud.QuestionCrud();
            for (int i = 0; i < questions.size(); i++) {
                questions.get(i).poll = pollid;

            }
            question.addQuestions(questions);

        }

        System.out.println("Insert is done successfully");

    }

    public void delete(int id) throws SQLException {
        try (Connection c = conn.connect()) {
            String deleteSQL = "DELETE FROM poll WHERE pollid = ?";
            try (PreparedStatement delete = c.prepareStatement(deleteSQL)) {
                delete.setInt(1, id);
                delete.executeUpdate();
                System.out.println("delete is done successfully");
                delete.close();
            }

            c.close();
        }

    }

    public List<Poll> selectall() throws SQLException {
        ResultSet resultSet;
        List<Poll> polls = new ArrayList<>();
        try (Connection c = conn.connect()) {
            String selectSQL = "select * from poll";
            try (PreparedStatement select = c.prepareStatement(selectSQL)) {
                resultSet = select.executeQuery();
                while (resultSet.next()) {
                    Poll poll = new Poll();
                    poll.title = resultSet.getString("title");
                    poll.pollid = resultSet.getInt("pollid");
                    poll.user = resultSet.getInt("user");
                    poll.aissuspended = resultSet.getBoolean("aissuspended");
                    poll.uissuspended = resultSet.getBoolean("uissuspended");
                    poll.close = resultSet.getBoolean("close");

                    polls.add(poll);
                }
                System.out.println("Selection is done successfully");
                select.close();
                c.close();

                return polls;

            }
        }

    }

    public Poll selectPollWithEverything(int id) throws SQLException {
        ResultSet resultSet;
        Poll poll = new Poll();
        try (Connection c = conn.connect()) {
            String selectSQL = "select * from poll where pollid = ?";

            try (PreparedStatement select = c.prepareStatement(selectSQL)) {
                select.setInt(1, id);
                resultSet = select.executeQuery();
                while (resultSet.next()) {
                    poll.title = resultSet.getString("title");
                    poll.pollid = resultSet.getInt("pollid");
                    poll.user = resultSet.getInt("user");
                    poll.aissuspended = resultSet.getBoolean("aissuspended");
                    poll.uissuspended = resultSet.getBoolean("uissuspended");
                    poll.close = resultSet.getBoolean("close");

                }
                crud.QuestionCrud questionCrud = new crud.QuestionCrud();
                List<Question> temp = questionCrud.selectByPollId(id);
                LinkedList<Question> result = new LinkedList<>();
                for(int i = 0 ; i < temp.size() ; i++){
                   result.add( temp.get(i) );
                }
                poll.questions = result;
                System.out.println("Selection is done successfully");
                select.close();
                c.close();

                return poll;

            }
        }

    }

    public List<Poll> selectByUserId(int id) throws SQLException {
        ResultSet resultSet;

        List<Poll> polls = new ArrayList<>();
        try (Connection c = conn.connect()) {
            String selectSQL = "SELECT * FROM poll WHERE user = ? ";
            try (PreparedStatement select = c.prepareStatement(selectSQL)) {
                select.setInt(1, id);
                resultSet = select.executeQuery();
                while (resultSet.next()) {
                    Poll poll = new Poll();
                    poll.title = resultSet.getString("title");
                    poll.pollid = resultSet.getInt("pollid");
                    poll.user = resultSet.getInt("user");
                    poll.aissuspended = resultSet.getBoolean("aissuspended");
                    poll.uissuspended = resultSet.getBoolean("uissuspended");
                    poll.close = resultSet.getBoolean("close");

                    polls.add(poll);
                }
                System.out.println("Selection is done successfully");
                select.close();
                c.close();
                return polls;

            }
        }

    }

    public void suspend(boolean isAdmin, int pollId) throws SQLException {
        if (isAdmin) {
            try (Connection c = conn.connect(); PreparedStatement suspend = c.prepareStatement("UPDATE poll SET aissuspended = ? WHERE pollid = ?")) {
                suspend.setBoolean(1, true);
                suspend.setInt(2, pollId);
                suspend.executeUpdate();
                suspend.close();
                c.close();
            }
        } else {
            try (Connection c = conn.connect(); PreparedStatement suspend = c.prepareStatement("UPDATE poll SET uissuspended = ? WHERE pollid = ?")) {
                suspend.setBoolean(1, true);
                suspend.setInt(2, pollId);
                suspend.executeUpdate();
                suspend.close();
                c.close();
            }
        }

    }

    public void unSuspend(boolean isAdmin, int pollId) throws SQLException {
        if (isAdmin) {
            try (Connection c = conn.connect(); PreparedStatement suspend = c.prepareStatement("UPDATE poll SET aissuspended = ? WHERE pollid = ?")) {
                suspend.setBoolean(1, false);
                suspend.setInt(2, pollId);
                suspend.executeUpdate();
                suspend.close();
                c.close();
            }
        } else {
            try (Connection c = conn.connect(); PreparedStatement suspend = c.prepareStatement("UPDATE poll SET uissuspended = ? WHERE pollid = ?")) {
                suspend.setBoolean(1, false);
                suspend.setInt(2, pollId);
                suspend.executeUpdate();
                suspend.close();
                c.close();
            }
        }

    }

    public void close(int pollId) throws SQLException {

        try (Connection c = conn.connect(); PreparedStatement close = c.prepareStatement("UPDATE poll SET close = ? WHERE pollid = ?")) {
            close.setBoolean(1, true);
            close.setInt(2, pollId);
            close.executeUpdate();
            close.close();
            c.close();

        }

    }

    public void open(int pollId) throws SQLException {

        try (Connection c = conn.connect(); PreparedStatement open = c.prepareStatement("UPDATE poll SET close = ? WHERE pollid = ?")) {
            open.setBoolean(1, false);
            open.setInt(2, pollId);
            open.executeUpdate();
            open.close();
            c.close();

        }

    }

}
