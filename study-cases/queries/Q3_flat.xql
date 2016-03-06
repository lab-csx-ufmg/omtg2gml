(: Os ids dos trechos que interceptam o bairro centro :)

<streetSegments>
	LET $doc := document("city.xml")
	FOR $n in $doc//neighborhood,
			$t in $doc//thoroughfare,
			$s in $doc//streetSegment
	WHERE $n/name = "centro" AND $n/interceptsNT/@ref = $t/name AND $t/aggregationTS/@ref = $s/id
	RETURN
	<streetSegment>
		<id>{$s/id/text()}</id>
	</streetSegment>
<streetSegments>