(: A quantidade de trechos dos logradouros do bairro funcionarios :)

<thoroughfares>
	LET $doc := document("city.xml")
	FOR $n in $doc//neighborhood,
			$t in $doc//thoroughfare,
			$s in $doc//streetSegment
	WHERE $n/name = "funcionarios" AND $n/interceptsNT/@ref = $t/name AND $t/aggregationTS/@ref = $s/id
	RETURN
	<thoroughfare>
		<name>{$t/name/text()}</name>
		<numStreetSegments>{count($s/id/text())}</numStreetSegments>
	</thoroughfare>
<thoroughfares>