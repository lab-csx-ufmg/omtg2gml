(: As areas ordenadas dos bairros da regiao centro-sul
   Spatial Operator: Area :)

<neighborhoods>
	LET $doc := document("city.xml")
	FOR $r in $doc//region
	LET $area := Area($r/aggregationRN/neighborhood/gml:Polygon)
	WHERE $r/name = "CENTRO-SUL"
	ORDER BY $area
	RETURN
	<neighborhood>
		<name>{$r/aggregationRN/neighborhood/name/text()}</name>
		<area>{$area}</area>
	</neighborhood>
<neighborhoods>