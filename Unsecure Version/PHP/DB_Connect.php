<?php
	class DB_Connect {

		function __construct() {
		
		}

		function __destruct() {
			// $this->close();
		}

		public function connect() {
			require_once 'config.php'; // Get database variables
			$options = array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8');

			try
			{
				$db = new PDO("mysql:host={$DB_HOST};dbname={$DB_NAME};charset=utf8", $DB_USER, $DB_PASSWORD, $options);
			}
			catch (PDOException $ex)
			{
				die("Failed to connect to the database");
			}

			$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			$db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);

			if(function_exists('get_magic_quotes_gpc') && get_magic_quotes_gpc())
			{
				function undo_magic_quotes_gpc(&$array)
				{
					foreach($array as &$value)
					{
						if(is_array($value))
						{
							undo_magic_quotes_gpc($value);
						}
						else
						{
							$value = stripslashes($value);
						}
					}
				}

				undo_magic_quotes_gpc($_POST);
				undo_magic_quotes_gpc($_GET);
				undo_magic_quotes_gpc($_COOKIE);
			}
			return $db;
		}

		public function close() {
			mysql_close();
		}

	}
?>