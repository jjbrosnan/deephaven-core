---
title: Deploy Command Line Tools with Python
sidebar_label: Command line tools
---

Python enables you to [create and package command line tools](https://packaging.python.org/en/latest/guides/creating-command-line-tools/) so that you can call Python programs like you would call shell commands. You can leverage this capability for Deephaven to create Command Line Interface (CLI) tools that spin up Deephaven servers & clients for applications quickly.

The example in this guide uses [pip-installed Deephaven](../getting-started/pip-install.md), as it allows you to start a Deephaven server via either Python or the CLI. A CLI tool packaged this way could also use the [Deephaven Python client](https://deephaven.io/core/client-api/python/) if a Deephaven server is already running and reachable.

## Prerequisites

These Python CLI tools are installed with [`pipx`](https://pipx.pypa.io/stable/). Refer to their documentation for installation and usage instructions.

Additionally, since the Python project uses [`deephaven-server`](https://pypi.org/project/deephaven-server/), your machine must have Java 11+ and `JAVA_HOME` set properly.

## Create a Python project

Most Python projects create distributions in one way or another, typically through a `pyproject.toml`, `setup.py`, or `setup.cfg` file. This guide uses a `pyproject.toml` file for its example, but any of the file formats mentioned would work.

Start by creating a new directory for your project. This guide calls the project `MyDeephavenCLI`.

```bash
mkdir MyDeephavenCLI
cd MyDeephavenCLI
```

This project should have the same directory structure as [Python's CLI tool example](https://packaging.python.org/en/latest/guides/creating-command-line-tools/):

```bash
mkdir src
```

### Requirements

Create a `requirements.txt` file in the root directory of the project. Like other Python projects, this file contains the project's dependencies. This project has only a single dependency:

- [`deephaven-server`](https://pypi.org/project/deephaven-server/): pip-installed Deephaven
- [`typer`](https://pypi.org/project/typer/): A Python library for building command line interfaces

So, your `requirements.txt` file should look like this:

```txt
deephaven-server>=0.39.8
typer>=0.17.4
```

> **_NOTE:_** This guide uses `0.39.8` or later as the minimum version of `deephaven-server`. You may use whatever version you wish. Deephaven recommends using the latest version of `deephaven-server`.

[`click`](https://click.palletsprojects.com/en/stable/) is another Python package that can be used to create CLI tools. Deephaven uses it for the [pip-installed Deephaven CLI](https://github.com/deephaven/deephaven-core/blob/main/py/embedded-server/deephaven_server/cli.py). [`typer`](https://typer.tiangolo.com/) is used for this example to mimic Python's official example.

### Source code

Next, head into the `src` directory, where the source code for the CLI tool will go. 

```bash
cd src
```

First, create the source code for the project. This guide calls the file `MyDeephavenCLI.py`.

```python
from deephaven_server import Server
from typing_extensions import Annotated
import typer

def my_deephaven_cli(
    port: Annotated[int, typer.Argument(help="The port number of the server.")] = 10000,
    memory_gb: Annotated[int, typer.Argument(help="The amount of memory in GB to allocate to the server.")] = 4,
    auth_type: Annotated[str, typer.Argument(help="The authentication type to use for the server.")] = "psk",
    password: Annotated[str, typer.Argument(help="The pre-shared key to use for authentication.")] = "password",
):
    """
    Start a Deephaven server with the specified configuration and create a ticking table.
    """
    memory_arg = f"-Xmx{memory_gb}g"
    if auth_type.lower() == "anonymous":
        auth_arg = "-DAuthHandlers=io.deephaven.auth.AnonymousAuthenticationHandler"
    elif auth_type.lower() == "psk":
        auth_arg = f"-Dauthentication.psk={password}"
    else:
        raise ValueError(f"Invalid auth type. This application only supports 'anonymous' and 'psk'.")

    s = Server(
        port=port,
        jvm_args=[
            memory_arg,
            auth_arg,
        ],
    ).start()
    typer.echo(f"Deephaven server started on port {port}")

    
```