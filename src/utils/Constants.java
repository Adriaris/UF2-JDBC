package utils;

public class Constants {
	// Obtiene el siguiente ID disponible para una nueva carta de un jugador específico.
    public static final String GET_LAST_ID_CARD_SQL = "SELECT ifnull((SELECT max(id) FROM card WHERE id_player = ?), 0) + 1 AS next_id";
    
    // Obtiene la última carta jugada.
    public static final String GET_LAST_CARD_SQL = "SELECT card.* FROM card JOIN game ON card.id = game.id_card WHERE game.id = (SELECT MAX(id) FROM game)";
    
    // Verifica las credenciales del jugador y obtiene su información.
    public static final String GET_PLAYER_SQL = "SELECT * FROM player WHERE user = ? AND password = ?";
    
    // Obtiene las cartas de un jugador que no han sido jugadas.
    public static final String GET_CARDS_SQL = "SELECT * FROM card WHERE id_player = ? AND id NOT IN (SELECT id_card FROM game WHERE id_card IS NOT NULL)";
    
    // Obtiene los detalles de una carta específica por su ID.
    public static final String GET_CARD_SQL = "SELECT * FROM card WHERE id = ?";
    
 // Inserta en la tabla de cartas jugadas el ID de la carta jugada.
    public static final String SAVE_GAME_SQL = "INSERT INTO game (id_card) VALUES (?)";
    
 // Inserta una nueva carta en la base de datos con el ID del jugador, número y color.
    public static final String SAVE_CARD_SQL = "INSERT INTO card (id_player, number, color) VALUES (?, ?, ?)";
    
 // Elimina una carta específica de la base de datos usando su ID.
    public static final String DELETE_CARD_SQL = "DELETE FROM card WHERE id = ?";
    
 // Elimina todas las cartas asociadas a un jugador específico de la base de datos.
    public static final String CLEAR_DECK_SQL = "DELETE FROM card WHERE id_player = ?";
    
 // Incrementa el número de victorias de un jugador por 1.
    public static final String ADD_VICTORIES_SQL = "UPDATE player SET victories = victories + 1 WHERE id = ?";
    
 // Incrementa el número de juegos jugados por un jugador por 1.
    public static final String ADD_GAMES_SQL = "UPDATE player SET games = games + 1 WHERE id = ?";
    
    
    //METODOS ADICIONALES 
    //Este metodo me permite verificar si la carta existe antes de guardar el juego porque he tenido algun error 
    public static final String CHECK_CARD_EXISTS_SQL = "SELECT COUNT(*) FROM card WHERE id = ?";








}
