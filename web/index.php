<?php
	function TableOfContents($depth,$filename)
	{
	    //read in the file
	    $file = fopen($filename,"r");
	    $html_string = fread($file, filesize($filename));
	    fclose($file);
     
	    //get the headings down to the specified depth
	    $pattern = '/<h[2-'.$depth.']*[^>]*>.*?<\/h[2-'.$depth.']>/';
	    $whocares = preg_match_all($pattern,$html_string,$winners);
     
	    //reformat the results to be more usable
	    $old_rang=0;
	    $heads = '';
	    foreach($winners[0] as $title){
	        //avoid the Warnings
	        if (preg_match('/WARNING/',$title)!= 0){
	            continue;
	        }
	        //get the depth
	        $i = strpos($title,'<h');
	        $rang = substr($title,$i+2,$i+1);
#	        print_r('rang de '.$title.'='. $rang);
	        //make a hierarchy
	        $bebin_ol = '';
	        $end_ol = '';
	        if ($rang > $old_rang){
#	            print_r('rang='.$rang.' old='. $old_rang.'\n');
	            $bebin_ol = '<ol>';
	        }
	        elseif ($rang < $old_rang){
	            for ($i = 0; $i <($old_rang - $rang); $i++) {
	                $end_ol .= "</ol>\n";
	            }
	        }
	        $title = preg_replace('/<h([1-'.$depth.'])>/',$end_ol.$bebin_ol.'<li>',$title);
	        $title = preg_replace('/<\/h[1-'.$depth.']>/','</li>',$title);
	        $heads .= $title;
	        $old_rang = $rang;
	    }
	    $heads = str_replace('<a name="','<a href="#',$heads);
     
	    //plug the results into appropriate HTML tags
	    $contents = '<div id="toc"> 
	    <p id="toc-header">Contents</p>
	    '.$heads.'
	    </div>';
	    echo $contents;
	}  

    
    if (isset($_GET['page'])){
        $page = $_GET['page'];
    }
    else{
        $page = 'home.html';
    }
    
    include ('header.php');
    include ($page);
    include ('footer.html')
?>
