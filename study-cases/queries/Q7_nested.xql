(: Os trechos contidos nos bairros centro e lourdes
   Spatial Operator: Contains, startPoint and endPoint :)

<neighborhoods>
	LET $doc := document("city.xml")
	FOR $n in $doc//region/aggregationRN/neighborhood
	WHERE $n/name = "centro" OR $n/name = "lourdes" 
	RETURN
	<neighborhood>
		<name>{$n/name/text()}</name>
		<thoroughfares>
			<thoroughfare>{
				FOR $t in $doc//thoroughfare
				WHERE Contains($n/gml:Polygon, $t/aggregationTS/streetSegment/gml:LineString) = 1
				RETURN
				<name>{$t/name/text()}</name>
				<streetSegments>
					<streetSegment> {
						FOR $segment in $t/aggregationTS/streetSegment
						<id>{$segment/id/text()}</id>
						<startPoint>{}</startPoint>
						<endPoint>{}</endPoint>
					}</streetSegment>
				</streetSegments>
			}</thoroughfare>
		</thoroughfares>
	</neighborhood>
</neighborhoods>