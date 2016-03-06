(: As areas ordenadas dos bairros da regiao centro-sul
   Spatial Operator: Area :)

<neighborhoods>
	LET $doc := document("city.xml")
	FOR $r in $doc//region,
		   $n in $doc//neighborhood
	LET $area := Area($n/gml:Polygon)
	WHERE $r/name = "CENTRO-SUL" AND $r/aggregationRN/@ref = $n/name
	ORDER BY $area
	RETURN
	<neighborhood>
		<name>{$n/name/text()}</name>
		<area>{$area}</area>
	</neighborhood>
<neighborhoods>