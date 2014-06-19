<?php
/**
* Handles all API requests
* Accepts GET and POST
*
* Each request will be identified by TAG
* Response will be JSON data
*/

//Check for POST request
if (isset($_POST['tag']) && $_POST['tag'] != '') {
	//get tag
	$tag = $_POST['tag'];

	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();

	$response = array("tag" => $tag, "success" => 0, "error" => 0); //Array that will be returned

	if ($tag == 'login') {
		$username = $_POST['username'];
		$passdoodle = $_POST['passdoodle'];

		$user = $db->getUserByUsernameAndPassdoodle($username, $passdoodle);
		if ($user != false) { //user found
			$response["success"] = 1;
			$response["id"] = $user["id"];
			$response["user"]["name"] = $user["name"];
			$response["user"]["username"] = $user["username"];
			$response["user"]["email"] = $user["email"];
			echo json_encode($response);
		} else { //user not found
			$response["error"] = 1;
			$response["error_msg"] = "Incorrect username or passdoodle. ";
			echo json_encode($response);
		}
	} else if ($tag == 'register') {
		$name = $_POST['name'];
		$email = $_POST['email'];
		$username = $_POST['username'];
		$passdoodle = $_POST['passdoodle'];

		if ($db->doesUserExist($username)) { //username in system
			$response["error"] = 2;
			$response["error_msg"] = "Username already exists";
			echo json_encode($response);
		} else { //unique username
			$user = $db->storeUser($username, $passdoodle, $email, $name);
			if ($user) { //user successfully stored
				$response["success"] = 1;
				$response["id"] = $user["id"];
				$response["user"]["name"] = $user["name"];
				$response["user"]["email"] = $user["email"];
				$response["user"]["username"] = $user["username"];
				echo json_encode($response);
			} else { //user failed to store
				$response["error"] = 1;
				$response["error_msg"] = "Error occurred in registration";
				echo json_encode($response);
			}
		}

	} else {
		echo "Invalid Request";
	}

} else { //No POST request
	echo "Access Denied";
}
?>