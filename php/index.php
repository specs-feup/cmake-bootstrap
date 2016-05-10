<!DOCTYPE html>
<html>
<head>
	<meta charset=utf-8 />
	<title>Libs</title>
	<link rel="stylesheet" type="text/css" media="screen" href="css/master.css" />
	<!-- <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script> -->
	<!--[if IE]>
		<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
</head>
<body>

	<h1>YOUR TITLE HERE</h1>
 
	<h2>Available Libs</h2>
	
	<?php

	echo "<ul>\n";
	
	foreach (glob("*.cmake") as $file) {
		echo "<li><a href=". $file . " download>" . $file . "</a></li>\n";
	}
	
	echo "</ul>\n";
	?>
	
	<p>
		<a href="download_cmakes.php">Download all</a>
	</p>
	
	
	<h2>Compiled Libs List</h2>
<?php

	echo "<ul>\n";
	
	foreach (glob("*.zip") as $file) {
		echo "<li>" . $file . "</li>\n";
	}
	
	echo "</ul>\n";
	
?>
	

</body>
</html>