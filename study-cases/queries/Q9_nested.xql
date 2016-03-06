(: As construções privadas distantes até 1km da rua da bahia
   Spatial Operator: Distance :)

<privates>
	LET $doc := document("city.xml")
	FOR $t in $doc//thoroughfare,
			$b in $doc//building
	WHERE $t/name = "da bahia" AND Distance($t/aggregationTS/streetSegment/gml:LineString, $b/private/gml:Polygon) <= 1
	RETURN
	<private>
		<code>{$b/code/text()}</code>
	</private>
</privates>