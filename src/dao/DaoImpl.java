package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import utils.Constants;


import model.Card;
import model.Player;

public class DaoImpl implements Dao{

	private Connection conn;
	@Override
	public void connect() throws SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/juego_cartas";
            String user = "root";
            String password = "";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión establecida.");
        } catch (SQLException e) {
            throw new SQLException("Error conectando a la base de datos: " + e.getMessage());
        }
		
	}

	@Override
	public void disconnect() throws SQLException {
        if (conn != null) {
            conn.close();
            System.out.println("Conexión cerrada.");
        }
		
	}

	@Override
	public int getLastIdCard(int playerId) throws SQLException {
	    int nextId = 0; // Asumimos 0 como valor predeterminado si no se encuentra ningún ID.
	    String sql = Constants.GET_LAST_ID_CARD_SQL;

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, playerId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                nextId = rs.getInt("next_id");
	            }
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error al obtener el siguiente ID de carta: " + e.getMessage(), e);
	    }

	    return nextId;
	}

	@Override
	public Card getLastCard() throws SQLException {
		Card lastCard = null;
	    String sql = Constants.GET_LAST_CARD_SQL;

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                lastCard = new Card(
	                        rs.getInt("id"),
	                        rs.getString("number"),
	                        rs.getString("color"),
	                        rs.getInt("id_player")
	                );
	            }
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error al obtener la última carta jugada: " + e.getMessage(), e);
	    }

	    return lastCard;
	}

	@Override
	public Player getPlayer(String user, String pass) throws SQLException {
	    Player player = null;
	    String sql = Constants.GET_PLAYER_SQL;
	    
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, user);
	        stmt.setString(2, pass);
	        
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            player = new Player(
	                    rs.getInt("id"),
	                    rs.getString("name"),
	                    rs.getInt("games"),
	                    rs.getInt("victories"));
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error al obtener el jugador: " + e.getMessage());
	    }
	    return player;
	}

	@Override
	public ArrayList<Card> getCards(int playerId) throws SQLException {
	    ArrayList<Card> cards = new ArrayList<>();
	    String sql = Constants.GET_CARDS_SQL;

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, playerId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                cards.add(new Card(
	                    rs.getInt("id"),
	                    rs.getString("number"),
	                    rs.getString("color"),
	                    playerId
	                ));
	            }
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error al obtener las cartas del jugador: " + e.getMessage(), e);
	    }

	    return cards;
	}

	@Override
	public Card getCard(int cardId) throws SQLException {
	    Card card = null;
	    String sql = Constants.GET_CARD_SQL;

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, cardId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                card = new Card(
	                    rs.getInt("id"),
	                    rs.getString("number"),
	                    rs.getString("color"),
	                    rs.getInt("id_player")
	                );
	            }
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error al obtener la carta: " + e.getMessage(), e);
	    }

	    return card;
	}

	@Override
	public void saveGame(Card card) throws SQLException {
	    // Verificar si la carta existe antes de intentar guardar el juego
	    if (!existsCard(card.getId())) {
	        throw new SQLException("La carta con ID: " + card.getId() + " no existe, no se puede crear el juego.");
	    }

	    String sql = Constants.SAVE_GAME_SQL;

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, card.getId());

	        int affectedRows = stmt.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("Crear el juego falló, no se insertaron filas.");
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error al guardar el juego: " + e.getMessage(), e);
	    }
	}
	
	public boolean existsCard(int cardId) throws SQLException {
	    String sql = Constants.CHECK_CARD_EXISTS_SQL;
	    boolean exists = false;
	    
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, cardId);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next() && rs.getInt(1) > 0) {
	                exists = true;
	            }
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error al verificar la existencia de la carta: " + e.getMessage(), e);
	    }
	    
	    return exists;
	}



	@Override
	public void saveCard(Card card) throws SQLException {
	    String sql = Constants.SAVE_CARD_SQL;

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, card.getPlayerId());
	        stmt.setString(2, card.getNumber());
	        stmt.setString(3, card.getColor());

	        int affectedRows = stmt.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("La inserción de la carta falló, no se insertaron filas.");
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error al guardar la carta: " + e.getMessage(), e);
	    }
	}

	@Override
	public void deleteCard(Card card) throws SQLException {
	    String sql = Constants.DELETE_CARD_SQL;

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, card.getId());

	        int affectedRows = stmt.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("La eliminación de la carta falló, no se eliminaron filas.");
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error al eliminar la carta: " + e.getMessage(), e);
	    }
	}

	@Override
	public void clearDeck(int playerId) throws SQLException {
	    String sql = Constants.CLEAR_DECK_SQL;

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, playerId);

	        stmt.executeUpdate(); 
	    } catch (SQLException e) {
	        throw new SQLException("Error al limpiar el mazo del jugador: " + e.getMessage(), e);
	    }
	}

	@Override
	public void addVictories(int playerId) throws SQLException {
	    String sql = Constants.ADD_VICTORIES_SQL;

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, playerId);

	        int affectedRows = stmt.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("No se pudo actualizar el número de victorias para el jugador con ID: " + playerId);
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error al agregar victorias: " + e.getMessage(), e);
	    }
	}

	@Override
	public void addGames(int playerId) throws SQLException {
	    String sql = Constants.ADD_GAMES_SQL;

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, playerId);

	        int affectedRows = stmt.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("No se pudo actualizar el número de juegos jugados para el jugador con ID: " + playerId);
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error al agregar juegos: " + e.getMessage(), e);
	    }
	}

}
