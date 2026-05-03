#!/usr/bin/env sh
#
# Gradle startup script for UN*X
#

die () {
    echo
    echo "$*"
    echo
    exit 1
} >&2

# Resolve APP_HOME
app_path=$0
while [ -h "$app_path" ] ; do
    ls=$( ls -ld "$app_path" )
    link=$( expr "$ls" : '.*-> \(.*\)$' )
    if expr "$link" : '/.*' > /dev/null; then
        app_path="$link"
    else
        app_path=$( dirname "$app_path" )"/$link"
    fi
done
APP_HOME=$( cd "$( dirname "$app_path" )" && pwd -P )
APP_BASE_NAME=$( basename "$0" )
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "$( uname )" in
  CYGWIN* )     cygwin=true  ;;
  Darwin* )     darwin=true  ;;
  MSYS* | MINGW* ) msys=true ;;
  NONSTOP* )    nonstop=true ;;
esac

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD=$JAVA_HOME/jre/sh/java
    else
        JAVACMD=$JAVA_HOME/bin/java
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD=java
    if ! command -v java >/dev/null 2>&1
    then
        die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
fi

# Escape application args
save () {
    for i do printf %s\\n "$i" | sed "s/'/'\\\\''/g;1s/^/'/;\$s/\$/' \\\\/" ; done
    echo " "
}
APP_ARGS=$(save "$@")

# Collect all arguments for the java command;
#   * $DEFAULT_JVM_OPTS, $JAVA_OPTS, and $GRADLE_OPTS can contain fragments of
#     shell script including quotes and variable substitutions, so put them in
#     double quotes to make sure that they get re-expanded; and
#   * put everything else in single quotes, so that it's not re-expanded.
eval set -- $DEFAULT_JVM_OPTS '"$JAVA_OPTS"' '"$GRADLE_OPTS"' '"-Dorg.gradle.appname=$APP_BASE_NAME"' -classpath '"$CLASSPATH"' org.gradle.wrapper.GradleWrapperMain '"$APP_ARGS"'

exec "$JAVACMD" "$@"
