for f in *.svg; do
  convert -background none ./"$f" ./"${f%.svg}.png"
done
