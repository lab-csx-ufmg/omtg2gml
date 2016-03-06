(: Os ids dos trechos que interceptam o bairro centro :)

<streetSegments>
	LET $doc := document("city.xml")
	FOR $n in $doc//region/aggregationRN/neighborhood,
			$t in $doc//thoroughfare
	WHERE $n/name = "centro" AND $n/interceptsNT/@ref = $t/name
	RETURN
	<streetSegment>
		<id>{$t/aggregationTS/streetSegment/id/text()}</id>
	</streetSegment>
<streetSegments>