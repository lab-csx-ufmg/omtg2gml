(: O nome dos bairros da regi�o centro-sul :)

<neighborhoods>{
	LET $doc := document("city.xml")
	FOR $r in $doc//region,
		    $n in $doc//neighborhood
	WHERE $r/name = "centro-sul" AND $r/aggregationRN/@ref = $n/name
	RETURN
	<neighborhood>
		<name>{$n/name/text()}</name>
	</neighborhood>
}</neighborhoods>