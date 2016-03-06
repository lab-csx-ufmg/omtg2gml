(: Os trechos contidos nos bairros centro e lourdes
   Spatial Operator: Contains :)

<streetSegments>
	LET $doc := document("city.xml")
	FOR $n in $doc//neighborhood,
			$s in $doc//streetSegment
	WHERE $n/name = "centro" OR $n/name = "lourdes" AND Contains($n/gml:Polygon, $s/gml:LineString) = 1
	RETURN
	<streetSegment>
		<neighborhood>{$n/name/text()}</neighborhood>
		<id>{ 
			FOR $id in $s/id/text()
			RETURN $id
		}</id>
	</streetSegment>
</streetSegments>