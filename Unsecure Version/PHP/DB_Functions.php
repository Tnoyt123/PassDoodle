<?php
/**
 * Contains the functions to CRUD the database contents
 */

class DB_Functions {

	private $db;

	function __construct() {
		require_once 'DB_Connect.php';

		$this->db = new DB_Connect();
		$this->db->connect();
	}

	function __destruct() {

	}

	// Add new user
	// Returns added row
	public function storeUser($username, $passdoodle, $email, $name) {
		$query = "
			INSERT INTO ususers (
				username,
				passdoodle,
				email,
				name
				) VALUES (
				:username,
				:passdoodle,
				:email,
				:name
				)
		";

		$query_params = array(
			':username' => $username,
			':passdoodle' => $passdoodle,
			':email' => $email,
			':name' => $name
			);

		try {
			$stmt = $this->db->prepare($query);
			$result = $stmt->execute($query_params);
		}
		catch (PDOException $ex)
		{
			return false;
		}
		if ($stmt->rowCount() > 0) {
			 $select_query = "
			 	SELECT * 
			 	FROM ususers
			 	WHERE
			 	username = :username
			 ";
			 $select_query_params = array(':username' => $username);

			 $stmt = $this->db->prepare($select_query);
			 $stmt->execute($select_query_params);
			 return $stmt->fetch();
		} else {
			return false;
		}
	}

	/**
	 * Get user by username and password
	 */
	public function getUserByUsernameAndPassdoodle($username, $passdoodle) {
		$query = "
			SELECT *
			FROM ususers
			WHERE
			username = :username
		";

		$query_params = array(
			':username' = $username;
		);

		try {
			$stmt = $this->db->prepare($query);
			$stmt->execute($query_params) or die("Error executing query. ");
		} 
		catch (PDOException $ex) {
			die ("PDOException thrown.");
		}

		$no_of_rows = $stmt->rowCount();  // rowCount() only works for SELECT statements on MySQL
		if ($no_of_rows > 0) {
			$result = $stmt->fetch();
			$stored_passdoodle = $result['passdoodle'];

			$split_saved_passdoodle = explode(";", $stored_passdoodle);
			$split_input_doodle = explode(";", $passdoodle);

			$tolerance = 0.075; //Adjustable for increased/decreased tolerance

			// Loop over $passdoodle and $stored_passdoodle to ensure each coordinate is within tolerable range
			for ($i = 0; $i < 100; $i++) { //100 points per saved PassDoodle
				$split_saved_coords = explode(",", $split_saved_passdoodle[$i]);
				$split_input_coords = explode(",", $split_input_doodle);

				$saved_x = $split_saved_coords[0];
				$saved_y = $split_saved_coords[1];

				$input_x = $split_input_coords[0];
				$input_y = $split_input_coords[1];

				if (sqrt(pow(($input_x-$saved_x),2) + pow(($input_y-$saved_y),2)) > $tolerance) {
					return false; // Any points that fall outside the tolerable range will immediately fail the entire comparison
				}
			}
			return $result;
		} else { // No entry found for $username
			return false;
		}
	}

	/**
	 * Check that a given username exists within the database
	 */
	public function doesUserExist($username) {
		$query = "
			SELECT
			username
			FROM ususers
			WHERE
			username = :username
		";

		$query_params = array(
			':username' => $username
			);

		$stmt = $this->db->prepare($query);
		$stmt->execute($query_params);

		if($stmt->rowCount() > 0) {
			//user exists
			return true;
		} else {
			//user does not exist
			return false;
		}

	}

}
?>