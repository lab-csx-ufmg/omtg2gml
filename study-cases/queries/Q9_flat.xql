(: As construções privadas distantes até 1km da rua da bahia
   Spatial Operator: Distance :)

<privates>
	LET $doc := document("city.xml")
	FOR $t in $doc//thoroughfare,
			$s in $doc//streetSegment,
			$b in $doc//building
	WHERE $t/name = "da bahia" AND $t/aggregationTS/@ref = $s/id AND Distance($s/gml:LineString, $b/private/gml:Polygon) <= 1
	RETURN
	<private>
		<code>{$b/code/text()}</code>
	</private>
</privates>