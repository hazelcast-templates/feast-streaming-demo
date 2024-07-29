
set -eu

bye () {
    if [[ "${1:-}" != "" ]]; then
        echo "ERROR $*" 1>&2
    fi
    exit 1
}

detect_platform () {
    arch="$(uname -m)"
    case "$arch" in
        x86_64*) arch=amd64;;
        amd64*) arch=amd64;;
        armv6l*) arch=arm;;
        armv7l*) arch=arm;;
        arm64*) arch=arm64;;
        aarch64*) arch=arm64;;
        *) bye "This script supports only 64bit Intel and 32/64bit ARM architecture, not $arch"
    esac
    state_arch="$arch"
}

state_arch=
detect_platform

curl -L -o kafkactl.tar.gz https://github.com/deviceinsight/kafkactl/releases/download/v5.0.6/kafkactl_5.0.6_linux_${state_arch}.tar.gz
tar xf kafkactl.tar.gz
rm kafkactl.tar.gz CHANGELOG.md LICENSE.md
mkdir -p $HOME/.local/bin
mv kafkactl $HOME/.local/bin
