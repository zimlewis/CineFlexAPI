-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Episode] (
    [Id] UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_Episode PRIMARY KEY,
    [Title] NVARCHAR(100) NOT NULL,
    [Number] VARCHAR(10) NOT NULL,
    [Description] NVARCHAR(1000),
    [Url] VARCHAR(50),
    [ReleaseDate] DATE,
    [CreatedTime] DATETIME,
    [UpdatedTime] DATETIME,
    [Duration] INT NOT NULL,
    [View] INT NOT NULL CONSTRAINT DF_Episode_View DEFAULT 0,
    [OpeningStart] INT,
    [OpeningEnd] INT,
    [IsDeleted] BIT NOT NULL CONSTRAINT DF_Episode_IsDeleted DEFAULT 0,
    [CommentSection] UNIQUEIDENTIFIER NOT NULL CONSTRAINT UQ_Episode_CommentSection UNIQUE CONSTRAINT FK_Episode_CommentSection FOREIGN KEY REFERENCES [dbo].[CommentSection]([Id]),
    [Season] UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Episode_Season FOREIGN KEY REFERENCES [dbo].[Season]([Id])
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Episode];
-- +goose StatementEnd
