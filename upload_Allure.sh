#!/bin/bash
#ls -al *
API_NAME=$1
TEST_ENVIRONMENT=$2
TEST_RESULT_FOLDER=$3

PROJECT_ID=$API_NAME"-"$TEST_ENVIRONMENT
ALLURE_SERVER_LOC=*
ALLURE_RESULT_FOLDER=allure_results


UPLOAD_REPORTS_URL="send-results?project_id=$PROJECT_ID&force_project_creation=true"
GEN_REPORTS_URL="generate-report?project_id=$PROJECT_ID"
CLEAN_RESULTS_URL="clean-results?project_id=$PROJECT_ID"

echo "==================================================================="

echo "get newman command parameters....."
echo "==================================================================="

#source ${PWD}/newman.env

echo "cleaning up existed files and test artifacts before test starts......"
echo "====================================================================="



if [[ -f (AAA)UploadResult.json ]]; then
   echo "upload base 64 file existed ,and should be removed".

   `rm (AAA)UploadResult.json`
fi   

if [ -d $TEST_RESULT_FOLDER ]; then

  #yourfilenames=`ls $ALLURE_RESULT_FOLDER`
  declare -a arrVar

  for f in `ls $TEST_RESULT_FOLDER`
  do
    echo "Processing $f file..."

  # take action on each file. $f store current file name
    #content=$(cat $ALLURE_RESULT_FOLDER/$f| base64) | tr -d '\n\r'  -w 0
    content=$(base64  -i  $TEST_RESULT_FOLDER/$f  | tr -d '\n\r' )

    #content=$(base64   -i $ALLURE_RESULT_FOLDER/$f | tr -d '\n\r' )


    arrVar[${#arrVar[@]}]={'"'file_name'"':'"'$f'"','"'content_base64'"':'"'$content'"'}
  done

  prefix={'"'results'"':[
  echo "$prefix" >> (AAA)UploadResult.json
  for value in "${arrVar[@]}"
  do
     echo   "$value," | tee -a (AAA)UploadResult.json   
    
  done
  #remove the last ","
  if     [ -n "$(tail -c1 (AAA)UploadResult.json)" ]    # if the file has not a trailing new line.
  then
       truncate -s-1 (AAA)UploadResult.json           # remove one char as the question request.
  else
       truncate -s-2 (AAA)UploadResult.json           # remove the last two characters
       echo "" >> file              # add the trailing new line back
  fi
 
  echo "]}" >> (AAA)UploadResult.json


  # cat (AAA)UploadResult.json


 
  echo "start to clear   existed regression test results...."
  echo "================================================="

  curl  -X GET  \
       --location $ALLURE_SERVER_LOC$CLEAN_RESULTS_URL \
       --header 'Content-Type: application/json' \
       --header 'Accept: */*' \
       -k \
       --data ''
  
  sleep 20
  


  echo "start to upload  regression test and generate report...."
  echo "================================================="
  

  curl --location $ALLURE_SERVER_LOC$UPLOAD_REPORTS_URL \
       --header 'Content-Type: application/json' \
       --header 'Accept: */*' \
       -k \
       --data "$(cat (AAA)UploadResult.json)"
  
  sleep 30
  
  echo "start to  generate report...."
  echo "================================================="
  
  curl -X GET  \
       --location $ALLURE_SERVER_LOC$GEN_REPORTS_URL \
       --header 'Content-Type: application/json' \
       --header 'Accept: */*' \
       -k \
       --data ''

else 
  echo "allure results folder is not existed,cannot upload results.."
fi
