rm -rf ../remarkable_lobby/src/main/java/com/clue/fbs/
rm -rf ../remarkable_match/src/main/java/com/clue/fbs/
rm -rf ../remarkable_admin/app/com/clue/fbs/
rm -rf ../remarkable/Assets/Scripts/com/clue/fbs/

FILES=/Application/*
for f in *.fbs
do
    ./flatc --java -o ../remarkable_lobby/src/main/java/ $f
    ./flatc --java -o ../remarkable_match/src/main/java/ $f
    ./flatc --php  -o ../remarkable_admin/app/ $f
    ./flatc --csharp -o ../remarkable/Assets/Scripts/ $f
done
