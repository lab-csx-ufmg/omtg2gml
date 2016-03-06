(: A quantidade de trechos dos logradouros do bairro funcionarios :)

<thoroughfares>
	LET $doc := document("city.xml")
	FOR $n in $doc//region/aggregationRN/neighborhood,
			$t in $doc//thoroughfare
	WHERE $n/name = "funcionarios" AND $n/interceptsNT/@ref = $t/name
	RETURN
	<thoroughfare>
		<name>{$t/name/text()}</name>
		<numStreetSegments>{count($t/aggregationTS/streetSegment/id/text())}</numStreetSegments>
	</thoroughfare>
<thoroughfares>