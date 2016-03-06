(: Os nomes dos bairros da região centro-sul :)

<neighborhoods>{
	LET $doc := document("city.xml")
	FOR $r in $doc//region
	WHERE $r/name = "centro-sul"
	RETURN
	<neighborhood>
		<name>{$r/aggregationRN/neighborhood/name/text()}</name>
	</neighborhood>
}</neighborhoods>