<?php

    # create new zip opbject
    $zip = new ZipArchive();

    # create a temp file & open it
    $tmp_file = tempnam('.','');
    $zip->open($tmp_file, ZipArchive::CREATE);

    # loop through each file
    foreach(glob("*.cmake") as $file){
        # download file
        $download_file = file_get_contents($file);

        #add it to the zip
		$zip->addFromString(pathinfo ( $file, PATHINFO_BASENAME), $download_file);

    }

    # close zip
    $zip->close();

    # send the file to the browser as a download
	header('Content-type: application/zip');
    header('Content-disposition: attachment; filename=libs.zip');
    header('Content-Length: ' . filesize($tmp_file));
    readfile($tmp_file);
	
?>