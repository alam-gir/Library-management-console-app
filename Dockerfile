# ============================
#  Build Stage
# ============================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /build

# Copy everything EXCEPT data files first
COPY . .

# Compile all .java files
RUN mkdir out \
 && find . -name "*.java" > sources.txt \
 && javac @sources.txt -d out \
 && jar cfe app.jar Main -C out .


# ============================
#  Runtime Stage
# ============================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /build/app.jar ./app.jar

# Copy initial data once (data becomes persistent later via -v)
COPY data ./data

VOLUME ["/app/data"]

ENTRYPOINT ["java", "-jar", "app.jar"]